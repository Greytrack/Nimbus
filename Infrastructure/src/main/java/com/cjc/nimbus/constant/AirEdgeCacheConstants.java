package com.cjc.nimbus.constant;

/**
 * 缓存常量
 *
 * @author jiangkun@airedgesoft.com
 * @date 2024/7/10
 */
public class AirEdgeCacheConstants {
    private AirEdgeCacheConstants() {
    }
    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 工作日历缓存,参数:租户ID,工作日历ID
     */
    public static final String WORK_CALENDAR_CACHE_KEY = "airedge:work_calendar_cache:%s:%s";
}
