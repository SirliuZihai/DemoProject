package com.zihai.bean;

import com.zihai.dto.People;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
@Configuration
public class TestConfig {
   /* @Bean
    public JedisConnectionFactory JedisConnectionFactory(
            JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory JedisConnectionFactory = new JedisConnectionFactory(
                jedisPoolConfig);
        // 连接池
        JedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        // IP地址
        JedisConnectionFactory.setHostName("127.0.0.1");
        // 端口号
        JedisConnectionFactory.setPort(6379);
        //JedisConnectionFactory.setDatabase(4);
        // 如果Redis设置有密码
//		 JedisConnectionFactory.setPassword(password);
        // 客户端超时时间单位是毫秒
        JedisConnectionFactory.setTimeout(5000);
        System.out.println("JedisConnectionFactory:"+JedisConnectionFactory);
        return JedisConnectionFactory;
    }*/


    /**
     * JedisPoolConfig 连接池
     *
     * @return
     */
   /* @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(20);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(20);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(-1);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        //jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        //jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        //jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(false);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(false);
        // jedis调用returnObject方法时，是否进行有效检查
        jedisPoolConfig.setTestOnReturn(false);

        System.out.println("jedisPoolConfig:"+jedisPoolConfig);
        return jedisPoolConfig;
    }*/
   @Bean
   public People getPeple(RedisTemplate<String,String> redisTemplate){
       People p = new People("test",Integer.valueOf(redisTemplate.opsForValue().get("num9909")));
       return p;
   }
}
