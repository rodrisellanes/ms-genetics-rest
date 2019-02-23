package com.genetics.adn.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genetics.adn.daos.GeneticsDao;
import com.genetics.adn.exceptions.InternalServerError;
import com.genetics.adn.model.EvaluatedDNA;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

    private final GeneticsDao geneticsDao;
    private final Jackson2JsonRedisSerializer<EvaluatedDNA> redisSerializer;

    @Autowired
    public RedisMessageSubscriber(GeneticsDao geneticsDao) {
        this.geneticsDao = geneticsDao;
        redisSerializer = new Jackson2JsonRedisSerializer<>(EvaluatedDNA.class);
        redisSerializer.setObjectMapper(new ObjectMapper());
    }

    @Override
    public void onMessage(Message mensaje, @Nullable byte[] bytes) {

        Mono.just(mensaje.getBody())
                .map(redisSerializer::deserialize)
                .flatMap(adnLeido -> geneticsDao.saveADNIndividuo(
                        adnLeido.getAdn(),
                        adnLeido.isMutante())
                )
                .map(a -> new Exception(""))
                .onErrorMap(SerializationException.class, InternalServerError::new)
                .doOnError(InternalServerError.class, err -> log.error("Error en la deserializacion del EvaluatedDAN: {}", mensaje.toString()))
                .doOnSubscribe(sub -> log.info("Recibe mensaje en Queue (ADN_QUEUE)"))
                .delaySubscription(Duration.ofMillis(1L))
                .subscribeOn(Schedulers.elastic())
                .subscribe();
    }

}
