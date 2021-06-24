package com.zihai.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zihai.h2Client.test.RedisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    //order
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.one.database}")
    private int one_database;

    @Value("${spring.redis.zero.database}")
    private int zero_database;
//    @Value("${spring.redis.sentinel.nodes}")
//    private String sentinelNodes;
//    @Value("${spring.redis.nodes}")
//    private String redisNodes;
//    @Value("${spring.redis.sentinel.master}")
//    private String master;


    private static final int MAX_IDLE = 2; //最大空闲连接数
    private static final int MAX_TOTAL = 10; //最大连接数
    private static final long MAX_WAIT_MILLIS = 10000; //建立连接最长等待时间


    //配置工厂
    public RedisConnectionFactory connectionFactory(String host, int port, String password,int index) {
     /*   RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        String[] hosts = sentinelNodes.split(",");
        for(String redisHost : hosts){
            String[] item = redisHost.split(":");
            String ip = item[0];
            String port1 = item[1];
            configuration.addSentinel(new RedisNode(ip, Integer.parseInt(port1)));
        }
        configuration.setMaster(master);*/

       /* RedisClusterConfiguration config = new RedisClusterConfiguration();

        String[] sub = redisNodes.split(",");
        List<RedisNode> nodeList = new ArrayList<>(sub.length);
        String[] tmp;
        for (String s : sub) {
            tmp = s.split(":");
            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
        }
        config.setClusterNodes(nodeList);*/

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);

        if (!StringUtils.isEmpty(password)) {
            jedisConnectionFactory.setPassword(password);
        }

        if (index != 0) {
            jedisConnectionFactory.setDatabase(index);
        }

        jedisConnectionFactory.setPoolConfig(poolConfig());
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    //连接池配置
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxTotal(MAX_TOTAL);
        poolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
        poolConfig.setTestOnBorrow(false);
        return poolConfig;
    }

    //------------------------------------
    @Bean(name = "redisZeroTemplate")
    public RedisTemplate redisZeroTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(
                connectionFactory(host, port, password, zero_database));
        template.setValueSerializer(new StringRedisSerializer());
        LOGGER.info("redisZeroTemplate=="+template.toString());
        return template;
    }

    //------------------------------------
    @Bean(name = "redisOneTemplate")
    public RedisTemplate redisOneTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(
                connectionFactory(host, port, password, one_database));
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(jackson2JsonRedisSerializer);
        LOGGER.info("redisOneTemplate=="+template.toString());
        return template;
    }
    //替换默认原有的 name一定要写redisTemplate
    @Bean(name = "redisTemplate")
    public RedisTemplate RedisTemplateBean2(RedisConnectionFactory factory) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setEnableTransactionSupport(false);
        LOGGER.info("redisTemplate=="+redisTemplate.toString());
        return redisTemplate;
    }


}