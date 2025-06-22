package com.cjc.nimbus.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangkun@airedgesoft.com
 * @date 2024/6/13
 */
@Data
@Configuration
public class RedisConfigProperties {
    /**
     * Redis server password.
     */
    @Value("${spring.data.redis.password:}")
    private String password;
    /**
     * Redis server database.
     */
    @Value("${spring.data.redis.database:0}")
    private int database;
    /**
     * Redis server port.
     */
    @Value("${spring.data.redis.port:6379}")
    private int port;
    /**
     * Redis server host.
     */
    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.timeout:5000}")
    private long timeout;

    @Value("${spring.data.redis.lettuce.pool.shutdown-timeout:1000}")
    private long shutDownTimeout;

    @Value("${spring.data.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle:2}")
    private int minIdle;

    @Value("${spring.data.redis.lettuce.pool.max-active:8}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-wait:-1}")
    private String maxWait;
}
