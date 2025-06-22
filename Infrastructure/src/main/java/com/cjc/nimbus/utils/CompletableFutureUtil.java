package com.cjc.nimbus.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author: jiangkun@airedgesoft.com
 * @date: 2024/9/11 10:30
 */
@Slf4j
public class CompletableFutureUtil {

    private CompletableFutureUtil() {
    }

    /**
     * 安全获取CompletableFuture的结果
     *
     * @param future CompletableFuture
     * @param <T>    结果类型
     * @return 结果
     */
    public static <T> T safeGet(CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("线程中断", e);
        } catch (ExecutionException e) {
            log.error("执行异常", e);
        }
        return null;
    }
}
