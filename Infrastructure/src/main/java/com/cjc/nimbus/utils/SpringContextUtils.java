package com.cjc.nimbus.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author CJC
 * @version 1.0
 * @description Spring上下文获取工具类
 * @date 2024/7/15 下午4:28
 */
@Component
@Slf4j
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * 获得当前的ApplicationContext
     */
    @Getter
    private static ApplicationContext applicationContext = null;

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 根据名称拿到Bean
     *
     * @param name 名称
     * @return java.lang.Object
     * @author LiDong
     * @date 2020/11/20
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 从ApplicationContext中获得Bean并且转型
     *
     * @param clazz 目标类型
     * @return T
     * @author LiDong
     * @date 2020/11/20
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    @Override
    public synchronized void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(SpringContextUtils.applicationContext)) {
            log.info("=====>SpringContextUtils初始化开始...");
            SpringContextUtils.applicationContext = applicationContext;
            log.info("=====>SpringContextUtils初始化成功...");
        }
    }

    /**
     * 判断spring是否已经启动
     *
     * @return spring是否已经启动
     */
    public static boolean isSpringStarted() {
        return Objects.nonNull(SpringContextUtils.applicationContext);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        if (isSpringStarted()) {
            return applicationContext.getEnvironment().getProperty(key, targetType, defaultValue);
        }
        return defaultValue;
    }

    public static String getProperty(String key, String defaultValue) {
        if (isSpringStarted()) {
            return applicationContext.getEnvironment().getProperty(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 发送事件
     */
    public static void publishEvent(Object event) {
        if (isSpringStarted()) {
            applicationContext.publishEvent(event);
        }
    }
}
