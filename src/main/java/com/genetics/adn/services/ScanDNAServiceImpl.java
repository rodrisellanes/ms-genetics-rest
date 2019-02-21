package com.genetics.adn.services;

import com.genetics.adn.daos.GeneticsDao;
import com.genetics.adn.exceptions.ForbiddenMutantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

@Slf4j
@Service
public class ScanDNAServiceImpl {

    private final GeneticsDao geneticsDao;
    private final DNAEvaluator dnaEvaluator;

    @Autowired
    public ScanDNAServiceImpl(GeneticsDao geneticsDao, DNAEvaluator dnaEvaluator) {
        this.geneticsDao = geneticsDao;
        this.dnaEvaluator = dnaEvaluator;
    }

    public Mono<Void> getMutant(String[] adn) {
        return Mono.just(adn)
                .map(dnaEvaluator::isMutant)
                .flatMap(esMutante -> geneticsDao.saveADNIndividuo(adn, esMutante))
//                .flatMap(Function.identity()) REDIS (pub/sub)
                .handle(resultadoADN())
                .doOnSuccess(esMutante -> log.info("El ADN recibido pertenece a un individuo mutante"))
                .doOnError(err -> log.warn("El ADN recibido pertenece a un individuo humano"))
                .doOnSubscribe(sub -> log.info("Ejecutando servicio de analisis de ADN mutante"))
                .then();
    }

    private BiConsumer<Boolean, SynchronousSink<Object>> resultadoADN() {
        return (esMutante, sync) -> {
            if(esMutante) {
                sync.complete();
            } else {
                sync.error(new ForbiddenMutantException());
            }
        };
    }

}
