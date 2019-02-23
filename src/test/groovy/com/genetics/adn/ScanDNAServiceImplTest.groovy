package com.genetics.adn

import com.genetics.adn.exceptions.ForbiddenMutantException
import com.genetics.adn.exceptions.InternalServerError
import com.genetics.adn.model.DnaRequest
import com.genetics.adn.model.EvaluatedDNA
import com.genetics.adn.queue.RedisMessagePublisher
import com.genetics.adn.services.DNAEvaluator
import com.genetics.adn.services.ScanDNAServiceImpl
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class ScanDNAServiceImplTest extends Specification {

    def "Servicio recibe un ADN de mutante y debe responder un void" () {

        given:
        def dnaRequest = new DnaRequest()
        dnaRequest.setAdn(["TTTT", "GGAA", "CCCC", "AAAA"] as String[])

        def esMutante = true
        def evaluatedDNA = new EvaluatedDNA(dnaRequest.getAdn(), esMutante)

        def dnaEvaluator = Mock(DNAEvaluator) {
            isMutanta(_ as String[]) >> esMutante
        }

        def redisMessagePublisher = Mock(RedisMessagePublisher) {
            publish(_ as EvaluatedDNA) >> Mono.empty()
        }

        def scanDNAServiceImpl = new ScanDNAServiceImpl(dnaEvaluator, redisMessagePublisher)

        when:
        def result = scanDNAServiceImpl.getMutant(dnaRequest.getAdn())

        then:
        StepVerifier.create(result)
            .expectComplete()
    }

    def "Servicio recibe un ADN de humano y debe responder un error" () {

        given:
        def dnaRequest = new DnaRequest()
        dnaRequest.setAdn(["AAGT", "GGAA", "CCTG", "AAGA"] as String[])

        def esMutante = false
        def evaluatedDNA = new EvaluatedDNA(dnaRequest.getAdn(), esMutante)

        def dnaEvaluator = Mock(DNAEvaluator) {
            isMutanta(_ as String[]) >> esMutante
        }

        def redisMessagePublisher = Mock(RedisMessagePublisher) {
            publish(_ as EvaluatedDNA) >> Mono.empty()
        }

        def scanDNAServiceImpl = new ScanDNAServiceImpl(dnaEvaluator, redisMessagePublisher)

        when:
        def result = scanDNAServiceImpl.getMutant(dnaRequest.getAdn())

        then:
        StepVerifier.create(result)
                .expectError(ForbiddenMutantException)
    }



}
