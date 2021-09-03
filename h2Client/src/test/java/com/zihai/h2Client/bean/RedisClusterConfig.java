package com.zihai.h2Client.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(prefix="spring.redis.cluster",name="enable",havingValue="true")
@Configuration
public class RedisClusterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClusterConfig.class);

    @Value("${spring.redis.cluster.nodes}")
    private String redisNodes;


    private static final int MAX_IDLE = 2; //最大空闲连接数
    private static final int MAX_TOTAL = 10; //最大连接数
    private static final long MAX_WAIT_MILLIS = 10000; //建立连接最长等待时间

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
    @Bean(name = "redisClusterTemplate")
    public RedisClusterConnection redisClusterConnection() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        String[] sub = redisNodes.split(",");
        List<RedisNode> nodeList = new ArrayList<>(sub.length);
        String[] tmp;
        for (String s : sub) {
            tmp = s.split(":");
            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
        }
        config.setClusterNodes(nodeList);
        JedisConnectionFactory factory = new JedisConnectionFactory(config,poolConfig());
        factory.afterPropertiesSet();
        return factory.getClusterConnection();
    }
}