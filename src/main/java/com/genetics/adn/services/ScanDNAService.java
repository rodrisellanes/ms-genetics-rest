package com.genetics.adn.services;

import com.genetics.adn.exceptions.ForbiddenMutantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Service
public class ScanDNAService {

    private static final int NO_MUTANTE = 0;
    private static final int PATRON_MUTANTE_ENCONTRADO = 1;
    private static final int INTERVALO_LECTURA_NUCLEOTIDOS = 4;
    private static final int UMBRAL_ES_MUTANTE = 2;
    private final Map<String, Integer> secuenciasADNMutante;

    @Autowired
    public ScanDNAService() {

        secuenciasADNMutante = new HashMap<>();
        secuenciasADNMutante.put("AAAA", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("TTTT", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("GGGG", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("CCCC", PATRON_MUTANTE_ENCONTRADO);
    }

    public Mono<Void> getMutant(String[] adn) {
        return Mono.just(adn)
                .map(this::isMutant)
//                .flatMap(Function.identity()) REDIS (pub/sub)
                .handle(resultadoADN())
                .doOnSuccess(esMutante -> log.info("El ADN recibido pertenece a un individuo mutante"))
                .doOnError(err -> log.warn("El ADN recibido pertenece a un individuo humano"))
                .doOnSubscribe(sub -> log.info("Ejecutando servicio de analisis de ADN mutante"))
                .then();
    }

    private Boolean isMutant(String[] adn) {
        return buscarPatronesMutantes(getSecuenciasMultiplesADN(adn)) >= UMBRAL_ES_MUTANTE;
    }

    private Stream<String> getSecuenciasMultiplesADN(String[] adn) {
        log.info("Obteniendo las multiples secuencias de nucleotidos de forma (Horizontal, Vertical y Diagonales");
        return Stream.concat(
                    Stream.concat(
                            getSecuenciasDiagonales(adn),
                            getSecuenciasDiagonales(invertirADN(adn))
                    ),
                    Stream.concat(
                            getSecuenciasHorizontales(adn),
                            getSecuenciasVertiales(adn)
                    )
            );
    }

    private String[] invertirADN(String[] adn) {
        return Arrays.stream(adn)
                .map(sec -> new StringBuilder(sec).reverse().toString())
                .toArray(String[]::new);
    }

    private Stream<String> getSecuenciasDiagonales(String[] adn) {
        List<String> secuenciasDiagonalAdn = new LinkedList<>();
        int lengthArray  = adn.length - 1;
        int dimension = (lengthArray * 2);

        for(int g = 0 ; g <= dimension ; g++) {
            StringBuilder secuenciaDiagonal = new StringBuilder();
            for(int i = g, j = 0 ; j <= g ; i-- , j++) {
                if(i <= lengthArray && j <= lengthArray)
                    secuenciaDiagonal.append(adn[i].charAt(j));
            }
            secuenciasDiagonalAdn.add(secuenciaDiagonal.toString());
        }
        return secuenciasDiagonalAdn.stream()
                .filter(secuencia -> secuencia.length() >= INTERVALO_LECTURA_NUCLEOTIDOS);
    }

    private Stream<String> getSecuenciasVertiales(String[] adn) {
        List<String> secuenciasVertiales = new LinkedList<>();

        for(int c = 0 ; c < adn.length - 1 ; c++) {
            StringBuilder secuenciaV = new StringBuilder();
            for(int f = 0 ; f < adn.length - 1 ; f++) {
                secuenciaV.append(adn[f].charAt(c));
            }
            secuenciasVertiales.add(secuenciaV.toString());
        }
        return secuenciasVertiales.stream()
                .filter(sec -> sec.length() >= INTERVALO_LECTURA_NUCLEOTIDOS);
    }

    private Stream<String> getSecuenciasHorizontales(String[] adn) {
        return Arrays.stream(adn)
                .filter(sec -> sec.length() >= INTERVALO_LECTURA_NUCLEOTIDOS);
    }

    private Integer buscarPatronesMutantes(Stream<String> secuenciasMultiples) {
        return secuenciasMultiples
                .peek(sec -> log.info("SecuenciaMultiple: {}", sec))
                .map(scanIntervalosDeNucleotidos())
                .reduce((a, b) -> a + b)
                .orElse(NO_MUTANTE);
    }

    private Function<String, Integer> scanIntervalosDeNucleotidos() {
        return secuencia -> {
            int patronesMutantes = 0;
            for(int i = 0 ; i <= (secuencia.length() - 4) ; i++) {
                String subSecuencia = secuencia.substring(i, INTERVALO_LECTURA_NUCLEOTIDOS);
                if(secuenciasADNMutante.getOrDefault(subSecuencia, NO_MUTANTE) == PATRON_MUTANTE_ENCONTRADO) {
                    i += 3;
                    patronesMutantes++;
                }
            }
            return patronesMutantes;
        };
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
