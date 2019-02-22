package com.genetics.adn

import com.genetics.adn.services.DNAEvaluator
import spock.lang.Specification

class DNAEvaluatorTest extends Specification {

    def "Envia un codigo de ADN de un mutante y el algoritmo debe devolver true" () {
        given:
        String[] adn = ["AAGC", "GGGG", "ACGT", "AGGA"]

        def adnEvaluated = new DNAEvaluator()

        when:

        def resultado = adnEvaluated.isMutant(adn)

        then:
        assert resultado

    }

}
