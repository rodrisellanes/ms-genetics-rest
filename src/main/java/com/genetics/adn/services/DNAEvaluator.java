package com.genetics.adn.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Component
public class DNAEvaluator {

    private static final int NO_MUTANTE = 0;
    private static final int PATRON_MUTANTE_ENCONTRADO = 1;
    private static final int INTERVALO_LECTURA_NUCLEOTIDOS = 4;
    private static final int UMBRAL_ES_MUTANTE = 2;
    private final Map<String, Integer> secuenciasADNMutante;

    @Autowired
    public DNAEvaluator() {
        secuenciasADNMutante = new HashMap<>();
        secuenciasADNMutante.put("AAAA", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("TTTT", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("GGGG", PATRON_MUTANTE_ENCONTRADO);
        secuenciasADNMutante.put("CCCC", PATRON_MUTANTE_ENCONTRADO);
    }

    protected Boolean isMutant(String[] adn) {
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

    private Stream<String> getSecuenciasHorizontales(String[] adn) {
        return Arrays.stream(adn)
                .filter(sec -> sec.length() >= INTERVALO_LECTURA_NUCLEOTIDOS);
    }

    private Stream<String> getSecuenciasVertiales(String[] adn) {
        List<String> secuenciasVertiales = new LinkedList<>();

        for(int c = 0 ; c < adn.length ; c++) {
            StringBuilder secuenciaV = new StringBuilder();
            for (String anAdn : adn) {
                secuenciaV.append(anAdn.charAt(c));
            }
            secuenciasVertiales.add(secuenciaV.toString());
        }
        return secuenciasVertiales.stream()
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
}
