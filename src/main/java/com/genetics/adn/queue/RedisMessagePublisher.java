package com.genetics.adn.queue;

import com.genetics.adn.model.EvaluatedDNA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessagePublisher implements MessagePublisher {

    private RedisTemplate<String, EvaluatedDNA> redisTemplate;
    private ChannelTopic topic;

    @Autowired
    public RedisMessagePublisher(RedisTemplate<String, EvaluatedDNA> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(EvaluatedDNA mensaje) {
        redisTemplate.convertAndSend(topic.getTopic(), mensaje);
    }
}
