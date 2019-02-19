package com.genetics.adn.resources;

import com.genetics.adn.daos.GeneticsDao;
import com.genetics.adn.model.StatsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class StatsDNAResource {

    private final GeneticsDao geneticsDao;

    @Autowired
    public StatsDNAResource(GeneticsDao geneticsDao) {
        this.geneticsDao = geneticsDao;
    }


    @GetMapping(value = "/api/v1.0/genetics/stats",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StatsResponse> getStatsDNA() {

        return geneticsDao.getADNStats()
                .doOnSuccess(stats -> log.info("Resultado de AND's evaluados: {}", stats))
                .doOnError(err -> log.error("No se completo la operacion (stats) por un error: {}", err))
                .doOnSubscribe(sub -> log.info("API - Stats: consulta ADN's evaluados"));
    }
}
