package com.cjc.nimbus.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public synchronized void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        if (MessageUtils.applicationContext == null) {
            MessageUtils.applicationContext = applicationContext;
        }
    }

    public static String getI18nMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return getI18nMessage(code, locale, args);
    }

    public static String getI18nMessage(String code, Locale locale, Object... args) {
        try {
            return applicationContext.getMessage(code, args, locale);
        } catch (Exception e) {
            return code;
        }
    }
}
