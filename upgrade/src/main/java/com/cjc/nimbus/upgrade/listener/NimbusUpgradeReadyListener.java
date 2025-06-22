package com.cjc.nimbus.upgrade.listener;

import com.cjc.nimbus.upgrade.config.FlywayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author : CJC
 * @date : 2023/4/19 16:05
 * @version : 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NimbusUpgradeReadyListener implements ApplicationListener<ContextClosedEvent> {

    private final Flyway flyway;

    private final FlywayConfig flywayConfig;

    /**
     * 项目启动自动执行
     */
    @EventListener(ApplicationReadyEvent.class)
    public void executeFlywayMigration() {
        log.info("executeFlywayMigration start");
        try {
            if (!flywayConfig.isCleanDisabled()) {
                flyway.clean();
            }
            flyway.migrate();
        } catch (FlywayException e) {
            log.error("Flyway配置第一次加载出错",e);
            try {
                flyway.repair();
                log.info("Flyway配置修复成功");
                flyway.migrate();
                log.info("Flyway配置重新加载成功");
            } catch (Exception e1){
                log.error("Flyway配置第二次加载出错", e1);
                this.closeApplication(1);
                return;
            }
        } catch (Exception e) {
            log.error("Flyway升级脚本执行出错", e);
            this.closeApplication(1);
            return;
        }
        log.info("executeFlywayMigration end");
        this.closeApplication(0);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 应用程序关闭前执行
        log.info("Application is closing...");
    }

    /**
     * 关闭应用程序
     */
    private void closeApplication(int status) {
        System.exit(status);
    }
}
