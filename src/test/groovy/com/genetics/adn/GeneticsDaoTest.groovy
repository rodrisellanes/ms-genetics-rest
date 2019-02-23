package com.genetics.adn

import com.genetics.adn.daos.GeneticsDao
import com.genetics.adn.exceptions.DataBaseConnectionException
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import reactor.test.StepVerifier
import spock.lang.Specification

class GeneticsDaoTest extends Specification {

    def "Simula un error de conexion con la base de datos para metodos (insert y query)"() {

        given:

        def adn = ["AT", "CT"] as String[]
        def esMutante = false

        def jdbcTemplate = Mock(NamedParameterJdbcTemplate) {
            update(_ as String, _ as Map) >> {
                throw new DataAccessException("error") {
                    @Override
                    String getMessage() {
                        return super.getMessage()
                    }
                }
            }

            query(_ as String, _ as BeanPropertyRowMapper) >> {
                throw new DataAccessException("error") {
                    @Override
                    String getMessage() {
                        return super.getMessage()
                    }
                }
            }
        }

        def dao = new GeneticsDao(jdbcTemplate)

        when:
        def saveResult = dao.saveADNIndividuo(adn, esMutante)
        def queryResult = dao.saveADNIndividuo(adn, esMutante)

        then:
        StepVerifier.create(saveResult)
            .expectError(DataBaseConnectionException)

        StepVerifier.create(queryResult)
            .expectError(DataBaseConnectionException)

    }
}
