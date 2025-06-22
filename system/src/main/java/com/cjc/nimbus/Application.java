package com.cjc.nimbus;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author CJC
 */
@SpringBootApplication(scanBasePackages = {"com.cjc.nimbus"})
@Slf4j
@EnableCaching
@EnableTransactionManagement
@EnableAsync
@EnableConfigurationProperties
@MapperScan("com.cjc.nimbus.**.dao.persistence")
@EnableScheduling
public class Application {


    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(Application.class).getEnvironment();
        log.info("----------------------------------------------------------");
        log.info("服务端 '{}' 启动完成!", env.getProperty("spring.application.name"));
        log.info("环境(s): {}", (Object) env.getActiveProfiles());
        log.info("日志级别: {}", env.getProperty("logging.level.com.airedgesoft.ae.*"));
        log.info("----------------------------------------------------------");
    }

}
