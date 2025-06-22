package com.cjc.nimbus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 登录超时配置
 *
 * @author CJC
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "airedgesoft.tokenstore")
public class LoginTokenTimeoutProperties {

    /**
     * token 超时时间(秒):60L * 24半小时
     * 默认30分钟
     */
    private long timeout = 60L * 30;

    /**
     * 同步时间周期(秒)
     * 5分种
     */
    private long syncInterval = 60L * 5;

}
