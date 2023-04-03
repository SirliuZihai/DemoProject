package com.zihai.h2Client.bean;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisExpir extends KeyExpirationEventMessageListener {
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate2;

    public RedisExpir(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // 获取过期的key
        String s2 = (String) redisTemplate2.opsForList().index(expiredKey, 2);
        System.out.println("listenering__" + expiredKey + "=" + s2);
    }
}
