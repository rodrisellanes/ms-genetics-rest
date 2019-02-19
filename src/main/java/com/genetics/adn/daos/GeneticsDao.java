package com.genetics.adn.daos;

import com.genetics.adn.model.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GeneticsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GeneticsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mono<Integer> saveADNIndividuo(String[] adn) {

        // TODO
        return Mono.empty();
    }

    public Mono<StatsResponse> getStats() {

        // TODO
        return Mono.empty();
    }
}
