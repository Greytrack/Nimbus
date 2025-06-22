package com.cjc.nimbus.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Redisson工具类
 *
 * @author jiangkun@airedgesoft.com
 */
@Component
@AllArgsConstructor
@Slf4j
public class RedissonUtil {

    private final RedissonClient redissonClient;


    /**
     * 获取值
     *
     * @param key 键
     * @param <T>
     * @return 值
     */
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 获取值
     *
     * @param keys 键
     * @param <T>  类型
     * @return 值
     */
    public <T> List<T> getList(String[] keys) {
        Map<String, T> map = redissonClient.getBuckets().get(keys);
        return map.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public <T> void set(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置值(异步)
     *
     * @param key   键
     * @param value 值
     * @param <T>
     */
    public <T> void setAsync(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.setAsync(value);
    }

    /**
     * 删除值
     *
     * @param key 键
     */
    public void delete(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.delete();
    }

    /**
     * 删除值
     *
     * @param pattern
     */
    public void deleteByPatternAsync(String pattern) {
        redissonClient.getKeys().deleteByPatternAsync(pattern);
    }

    /**
     * 删除值
     *
     * @param pattern
     */
    public void deleteByPattern(String pattern) {
        redissonClient.getKeys().deleteByPattern(pattern);
    }


    /**
     * 删除值
     *
     * @param pattern
     */
    public <T> List<T> getByPattern(String pattern) {
        Map<String, T> map = redissonClient.getBuckets().get(redissonClient.getKeys().getKeysStreamByPattern(pattern + ":*").toArray(String[]::new));
        return new ArrayList<>(map.values());
    }

    /**
     * 是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean isExists(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.isExists();
    }

    /**
     * 如果不存在则设置 并返回 true 如果存在则返回 false
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return set成功或失败
     */
    public <T> boolean setObjectIfAbsent(final String key, final T value, final Duration duration) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.setIfAbsent(value, duration);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public boolean deleteObject(final String key) {
        return redissonClient.getBucket(key).delete();
    }


    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param <T>        数据类型
     */
    public <T> void publish(String channelKey, T msg) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publish(msg);
    }

    /**
     * 发布通道消息(异步)
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param <T>        数据类型
     */
    public <T> void publishAsync(String channelKey, T msg) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.publishAsync(msg);
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = redissonClient.getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }


}
