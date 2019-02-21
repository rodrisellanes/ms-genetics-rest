package com.genetics.adn.queue;

import com.genetics.adn.daos.GeneticsDao;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisMessageSubscriber implements MessageListener {

    private final GeneticsDao geneticsDao;

    @Autowired
    public RedisMessageSubscriber(GeneticsDao geneticsDao) {
        this.geneticsDao = geneticsDao;
    }


    @Override
    public void onMessage(Message mensaje, @Nullable byte[] bytes) {

        // TODO do some logic
        // FIXME have to user Jackson Deserializer
        try {
            System.out.println(Thread.currentThread().getName());
            System.out.println(mensaje.toString());
            Thread.sleep(2000L);
            Boolean mutante = Boolean.valueOf(mensaje.toString().split("=")[2].replace("\"", "").replace(")", ""));
            this.geneticsDao.saveADNIndividuo((mensaje).toString().split("="), mutante).subscribe();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
