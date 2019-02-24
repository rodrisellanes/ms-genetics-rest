package com.genetics.adn

import com.genetics.adn.services.DNAEvaluator
import spock.lang.Specification

class DNAEvaluatorTest extends Specification {

    def "Envia un codigo de ADN de un mutante y el algoritmo debe devolver true" () {
        given:

        def adnMutante1 = ["AAGC", "GGGG", "ACGT", "AGGA"] as String[]
        def adnMutante2 = ["TTGAC", "ATCCC", "ACTCC", "ATTTC", "ACTGG"] as String[]
        def adnMutante3 = ["TTTCCAG","CAAAGCG","CAACCTG","AAAAGGG","CACCTAG","GACTTGG","GACCCCG"] as String[]

        String[] adnHumano = ["AAGC", "GAAG", "ACGT", "CCGA"]

        def adnEvaluated = new DNAEvaluator()

        when:
        def resultadoMutante1 = adnEvaluated.isMutant(adnMutante1)
        def resultadoMutante2 = adnEvaluated.isMutant(adnMutante2)
        def resultadoMutante3 = adnEvaluated.isMutant(adnMutante3)
        def resultadoHumano = adnEvaluated.isMutant(adnHumano)

        then:
        assert resultadoMutante1
        assert resultadoMutante2
        assert resultadoMutante3
        assert !resultadoHumano
    }

}
