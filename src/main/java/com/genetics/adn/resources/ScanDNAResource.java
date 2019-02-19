package com.genetics.adn.resources;

import com.genetics.adn.exceptions.BadRequestMutantException;
import com.genetics.adn.model.DnaRequest;
import com.genetics.adn.services.ScanDNAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
@RestController
public class ScanDNAResource {

    private final ScanDNAService scanDNAService;

    @Autowired
    public ScanDNAResource(ScanDNAService scanDNAService) {
        this.scanDNAService = scanDNAService;
    }

    @PostMapping(value = "/api/v1.0/genetics/mutant",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> scanMutantDNA(@RequestBody DnaRequest adnRequest) {

        return Mono.just(adnRequest.getAdn())
                .map(this::validarSecuenciaCompleta)
                .doOnError(err -> log.error("ADN request invalido, no cumple con dimension matriz cuadrada"))
                .flatMap(scanDNAService::getMutant)
                .doOnSubscribe(sub -> log.info("API - Mutant: request DNA: {} para analisis de mutante", (Object) adnRequest.getAdn()));
    }

    private String[] validarSecuenciaCompleta(String[] adn) {
        Integer dimension = (adn.length) * (adn.length);
        Predicate<Integer> dimensionCuadrada = totalNucleotidos -> ((double) totalNucleotidos / dimension) == 1;
        Stream.of(adn)
                .map(String::length)
                .reduce((a, b) -> a + b)
                .filter(dimensionCuadrada)
                .orElseThrow(BadRequestMutantException::new);

        return adn;
    }

}
