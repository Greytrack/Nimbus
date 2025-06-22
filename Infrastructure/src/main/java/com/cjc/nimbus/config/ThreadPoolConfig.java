package com.cjc.nimbus.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 * @author: jiangkun@airedgesoft.com
 * @date: 2024/9/5 19:18
 */
@AllArgsConstructor
@Configuration
public class ThreadPoolConfig {

    private final int core = Runtime.getRuntime().availableProcessors() + 1;

    private final ThreadPoolProperties threadPoolProperties;

    @Bean(name = "threadPoolTaskExecutor")
    @ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(core * 2);
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * ttl线程池,配合TransmittableThreadLocal使用
     *
     * @return ExecutorService
     */
    @Bean(name = "commonExecutorService")
    @ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true")
    public ExecutorService ttlThreadPoolTaskExecutor() {
        return TtlExecutors.getTtlExecutorService(
                new ThreadPoolExecutor(
                        core,
                        core << 1,
                        threadPoolProperties.getKeepAliveSeconds(),
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(threadPoolProperties.getQueueCapacity()),
                        new ThreadFactoryBuilder().setNamePrefix("common-ttl-pool").build(),
                        new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    /**
     * ttl线程池,配合TransmittableThreadLocal使用
     *
     * @return ExecutorService
     */
    @Bean(name = "pxThreadPoolTaskExecutor")
    @ConditionalOnProperty(prefix = "thread-pool", name = "enabled", havingValue = "true")
    public ExecutorService pxThreadPoolTaskExecutor() {
        return TtlExecutors.getTtlExecutorService(
                new ThreadPoolExecutor(
                        core,
                        core << 1,
                        threadPoolProperties.getKeepAliveSeconds(),
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(threadPoolProperties.getQueueCapacity()),
                        new ThreadFactoryBuilder().setNamePrefix("px-ttl-pool").build(),
                        new ThreadPoolExecutor.CallerRunsPolicy()));
    }
}
