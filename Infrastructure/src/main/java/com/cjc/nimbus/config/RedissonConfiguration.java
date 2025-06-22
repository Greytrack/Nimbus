package com.cjc.nimbus.config;

import com.cjc.nimbus.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.Redisson;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangkun@airedgesoft.com
 * @date 2024/6/13
 */
@EnableCaching
@Configuration
public class RedissonConfiguration {

    @Bean
    public Redisson redisson(RedisProperties redisProperties) {
        String redissonAddress = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        Config config = new Config();
        SingleServerConfig singleConfig = config.useSingleServer().setAddress(redissonAddress).
                //心跳检测，定时与redis连接，可以防止一段时间过后，与redis的连接断开
                        setPingConnectionInterval(1000);
        // 订阅连接池大小
        singleConfig.setSubscriptionConnectionPoolSize(150);
        // 每个连接最大订阅数量
        singleConfig.setSubscriptionsPerConnection(50);
        if (StringUtils.isBlank(redisProperties.getPassword())){
            singleConfig.setPassword(null);
        } else {
            singleConfig.setPassword(redisProperties.getPassword());
        }

        // 设置序列化方式
        ObjectMapper objectMapper = new ObjectMapper();
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return (Redisson) Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.DAYS)
                .maximumSize(10000));
        return cacheManager;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedissonConnectionFactory redissonConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redissonConnectionFactory);
        return redisMessageListenerContainer;
    }




}
