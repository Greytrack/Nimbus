package com.cjc.nimbus.upgrade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 升级服务
 * @author CJC
 * @date 2025/6/22
 */
@SpringBootApplication(
        scanBasePackages = {"com.cjc.nimbus.upgrade.*"},
        exclude = {FlywayAutoConfiguration.class})
@Slf4j
@EnableTransactionManagement
@EnableAsync
public class UpgradeApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(UpgradeApplication.class).getEnvironment();
        log.info("----------------------------------------------------------");
        log.info("服务端 '{}' 启动完成!", env.getProperty("spring.application.name"));
        log.info("环境(s): {}", (Object) env.getActiveProfiles());
        log.info("日志级别: {}", env.getProperty("logging.level.com.cjc.nimbus.*"));
        log.info("----------------------------------------------------------");

    }

}
