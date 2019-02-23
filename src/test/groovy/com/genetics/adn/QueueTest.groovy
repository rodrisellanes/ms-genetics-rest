package com.genetics.adn

import com.genetics.adn.daos.GeneticsDao
import com.genetics.adn.exceptions.DataBaseConnectionException
import com.genetics.adn.model.StatsResponse
import com.genetics.adn.queue.RedisMessageSubscriber
import org.springframework.data.redis.connection.Message
import reactor.core.publisher.Mono
import spock.lang.Specification

class QueueTest extends Specification {

    def "Recibe un mensaje en la queue de redis y lo guarda en la base de datos" () {

        given:
        def evaluatedAdn = "{\"adn\":[\"AGAA\",\"AACC\",\"ACGC\",\"TTTT\"],\"mutante\":false}"

        def message = new Message() {
            @Override
            byte[] getBody() {
                return evaluatedAdn.getBytes()
            }

            @Override
            byte[] getChannel() {
                return [80, 80, 80]
            }
        }

        def statsResult = new StatsResponse()
        statsResult.setContadorAdnMutante(40)
        statsResult.setContadorAdnHumano(100)
        statsResult.setRatio(0.4)

        def geneticsDao = Mock(GeneticsDao) {
            saveADNIndividuo(_ as byte[], _ as boolean) >> Mono.just(true)
        }
        def redisMessageSubscriber = new RedisMessageSubscriber(geneticsDao)

        when:
        def result = redisMessageSubscriber.onMessage(message, evaluatedAdn.getBytes())

        then:
        result == null
    }
}
