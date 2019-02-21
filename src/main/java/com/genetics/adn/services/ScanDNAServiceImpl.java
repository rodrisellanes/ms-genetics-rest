package com.genetics.adn.services;

import com.genetics.adn.exceptions.ForbiddenMutantException;
import com.genetics.adn.model.EvaluatedDNA;
import com.genetics.adn.queue.RedisMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Service
public class ScanDNAServiceImpl {

    private final DNAEvaluator dnaEvaluator;
    private final RedisMessagePublisher redisPublisher;

    @Autowired
    public ScanDNAServiceImpl(DNAEvaluator dnaEvaluator, RedisMessagePublisher redisPublisher) {
        this.dnaEvaluator = dnaEvaluator;
        this.redisPublisher = redisPublisher;
    }

    public Mono<Void> getMutant(String[] adn) {
        return Mono.just(adn)
                .map(dnaEvaluator::isMutant)
                .map(esMutante -> new EvaluatedDNA(adn, esMutante))
                .map(publishAND())
                .handle(resultadoADN())
                .doOnSuccess(esMutante -> log.info("El ADN recibido pertenece a un individuo mutante"))
                .doOnError(err -> log.warn("El ADN recibido pertenece a un individuo humano"))
                .doOnSubscribe(sub -> log.info("Ejecutando servicio de analisis de ADN mutante"))
                .then();
    }

    private Function<EvaluatedDNA, EvaluatedDNA> publishAND() {
        return evaluatedDNA -> {
            redisPublisher.publish(evaluatedDNA);
            log.info("Publish de ADN en Redis OK");
            return evaluatedDNA;
        };
    }

    private BiConsumer<EvaluatedDNA, SynchronousSink<Object>> resultadoADN() {
        return (evaluatedDNA, sync) -> {
            if(evaluatedDNA.isMutante()) {
                sync.complete();
            } else {
                sync.error(new ForbiddenMutantException());
            }
        };
    }

}
