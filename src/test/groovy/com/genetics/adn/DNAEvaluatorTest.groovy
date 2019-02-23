package com.genetics.adn

import com.genetics.adn.services.DNAEvaluator
import spock.lang.Specification

class DNAEvaluatorTest extends Specification {

    def "Envia un codigo de ADN de un mutante y el algoritmo debe devolver true" () {
        given:
        String[] adnMutante = ["AAGC", "GGGG", "ACGT", "AGGA"]
        String[] adnHumano = ["AAGC", "GAAG", "ACGT", "CCGA"]
        def adnEvaluated = new DNAEvaluator()

        when:
        def resultadoMutante = adnEvaluated.isMutant(adnMutante)
        def resultadoHumano = adnEvaluated.isMutant(adnHumano)

        then:
        assert resultadoMutante
        assert !resultadoHumano
    }

}
