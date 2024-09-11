package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建 RedisTemplate ...");
        RedisTemplate redisTemplate = new RedisTemplate();

        // 设置连接工厂对象，获取 Redis 连接，与 Redis 服务器建立通讯
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置 redis key 的序列化器，将 Redis 中的二进制数据转换为字符串
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
