package com.cjc.nimbus.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CJC
 * @version 1.0
 * @description 添加配置文件，处理favicon.ico请求
 * @date 2024/7/8 下午4:20
 */
@SpringBootConfiguration
public class IconConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if (!"GET".equalsIgnoreCase(request.getMethod())
                        || !"/favicon.ico".equals(request.getRequestURI())) {
                    return true;
                }
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return false;
            }
        }).addPathPatterns("/**");
    }
}
