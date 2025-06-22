package com.cjc.nimbus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author CJC
 * @version 1.0
 * @description IdentifierGenerator配置
 * @date 2024/7/15 下午4:10
 */
@Configuration
public class IdGeneratorConfig {

    public static final long DATACENTER_ID = 1L;

    @Bean
    public SnowflakeIdGenerator idGenerator() {
        return new SnowflakeIdGenerator(getDatacenterId(), getWorkerId());
    }

    private long getWorkerId() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            return (hostAddress.hashCode() & 0x000000FF) %SnowflakeIdGenerator.getMaxMachineNum();
        } catch (Exception e) {
            return 0L;
        }
    }

    private long getDatacenterId() {
        return DATACENTER_ID;
    }
}
