package com.genetics.adn.daos;

import com.genetics.adn.exceptions.DataBaseConnectionException;
import com.genetics.adn.model.StatsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GeneticsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GeneticsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mono<Boolean> saveADNIndividuo(String[] adn, Boolean mutante) {
        Map<String, Object> params = new HashMap<>();
        params.put("adn", adn);
        params.put("mutante", mutante);

        return Mono.fromCallable(() -> {
            String insertSQL = "insert into adn.adn_evaluados(adn, mutante, fecha_creacion) values(:adn, :mutante, now())";
            return jdbcTemplate.update(
                    insertSQL,
                    params);
        })
                .doOnSuccess(idDb -> log.info("Insert en base de datos ejecutado exitosamente, id: {}, esMutante: {}", idDb, mutante))
                .map(idDb -> mutante)
                .onErrorMap(DataAccessException.class, DataBaseConnectionException::new)
                .doOnError(err -> log.error("Error en operacion insert ADN individo", err))
                .doOnSubscribe(sub -> log.info("Se ejecuta operacion insert (saveADN) de base da datos, individuoMutante: {}", mutante));
    }

    public Mono<StatsResponse> getADNStats() {

        return Mono.fromCallable(() -> {
            String querySql = "select sum(case when mutante then 1 end) contador_adn_mutante, sum(case when not mutante then 1 end) contador_adn_humano, sum(case when mutante then 1.0 else 0 end) / count(1) ratio from adn.adn_evaluados";
            return jdbcTemplate.query(
                    querySql,
                    new BeanPropertyRowMapper<>(StatsResponse.class));
        })
                .defaultIfEmpty(Collections.singletonList(StatsResponse.builder().build()))
                .map(queryResult -> queryResult.stream().findFirst().get())
                .doOnSuccess(stats -> log.info("Query en base de datos ejecutada exitosamente"))
                .onErrorMap(DataAccessException.class, DataBaseConnectionException::new)
                .doOnError(err -> log.error("Error en operacion getADNStats", err))
                .doOnSubscribe(sub -> log.info("Se ejecuta operacion query (getStats) de base da datos"));
    }
}
