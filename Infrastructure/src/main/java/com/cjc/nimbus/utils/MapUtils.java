package com.cjc.nimbus.utils;


import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author CJC
 * @version 1.0
 * @description MapUtil拓展工具类
 * @date 2024/11/6 17:30
 */
@Slf4j
public class MapUtils extends cn.hutool.core.map.MapUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据value获取key
     */
    public static String getKeyByValue(Map<String, Long> map, Long value) {
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static <T> List<T> convertObjectToListSubMap(Object source, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        if (source instanceof List) {
            List<?> elementList = (List<?>) source;
            for (Object element : elementList) {
                if (element instanceof Map) {
                    try {
                        // 将每个 Map 转换为目标对象
                        T obj = objectMapper.convertValue(element, clazz);
                        result.add(obj);
                    } catch (Exception e) {
                        // 处理转换异常
                        log.error("Error converting map to object: " + e.getMessage());
                    }
                }
            }
        } else {
            log.error("The elements object is not of expected type List<Map<String, Object>>");
        }

        return result;
    }

    /**
     * 将 JSON 字符串 转换为 Map
     */
    public static Map<String, Object> convertJsonStringToMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to map: " + e.getMessage());
            throw new AppException(GlobalResultCode.MYBATIS_EXECUTE_ERROR);
        }
    }
    /**
     * 将 Map 转换为 JSON 字符串
     *
     * @param map 要转换的 Map
     * @return 转换后的 JSON 字符串
     * @throws JsonProcessingException 如果转换过程中发生错误
     */
    public static String convertMapToJson(Map<?, ?> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON: " + e.getMessage());
            throw new AppException(GlobalResultCode.MYBATIS_EXECUTE_ERROR);
        }
    }

    /**
     * 将 Map 转换为格式化的 JSON 字符串
     *
     * @param map 要转换的 Map
     * @return 转换后的格式化的 JSON 字符串
     * @throws JsonProcessingException 如果转换过程中发生错误
     */
    public static String convertMapToPrettyJson(Map<?, ?> map) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }


}
