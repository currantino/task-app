package com.currantino.taskapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(
            @Value("${redis.host}")
            String host,
            @Value("${redis.port}")
            Integer port
    ) {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));//, clientConfig);
    }
}
