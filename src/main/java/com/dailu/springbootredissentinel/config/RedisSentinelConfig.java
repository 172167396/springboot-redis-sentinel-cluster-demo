package com.dailu.springbootredissentinel.config;

import io.lettuce.core.ReadFrom;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Profile("sentinel")
@Configuration
@ConfigurationProperties(prefix = "spring.redis.sentinel")
public class RedisSentinelConfig {
    @Value("spring.redis.timeout.sentinel.nodes")
    private Set<String> nodes;
    @Value("spring.redis.timeout.sentinel.master")
    private String master;

    @Value("${spring.redis.timeout}")
    private long timeout;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.sentinel.password}")
    private String sentinelPassword;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private long maxWait;
    @Value("${spring.redis.lettuce.pool.max-active}")
    private int maxActive;


    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(master, nodes);
        redisSentinelConfiguration.setPassword(password);
        redisSentinelConfiguration.setSentinelPassword(sentinelPassword);
        redisSentinelConfiguration.setMaster(master);
        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWait(Duration.of(maxWait, ChronoUnit.MILLIS));
        //readFrom(ReadFrom.REPLICA) 可设置，设置了就形成读写分离，读会读取从节点，但是因为有复制过程，要能容忍短时间的脏数据，适合对数据要求不太及时的
        LettucePoolingClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig).readFrom(ReadFrom.REPLICA)
                .build();
        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }


    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public void setMaster(String master) {
        this.master = master;
    }

}