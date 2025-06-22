package com.cjc.nimbus.utils;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.cjc.nimbus.utils.StringPool.NEW_VAL;
import static com.cjc.nimbus.utils.StringPool.OLD_VAL;


/**
 * 对象工具类
 *
 * @author jiangkun@airedgesoft.com
 */
@Slf4j
public class ObjectUtil {
    /**
     * 屏蔽字段
     */
    protected static final String[] SCRECT_FIELDS = {"password", "accessToken", "appToken"};

    private ObjectUtil() {
    }

    /**
     * 判断元素不为空
     *
     * @param obj object
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }


    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }


    /**
     * 将map转为字符串
     *
     * @param paramMap map
     * @return string
     */
    public static String mapToString(Map<String, String[]> paramMap) {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

            String key = param.getKey();
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            String obj = CharSequenceUtil.endWithAnyIgnoreCase(param.getKey(), SCRECT_FIELDS) ? "***" : paramValue;
            params.put(key, obj);
        }
        return JsonUtil.toJson(params);
    }

    /**
     * 将map转为字符串
     *
     * @param paramMap map
     * @return string
     */
    public static String mapToStringAll(Map<String, String[]> paramMap) {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {

            String key = param.getKey();
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.put(key, paramValue);
        }
        return JsonUtil.toJson(params);
    }

    /**
     * 将对象转为map
     *
     * @param obj 对象
     * @return map
     */
    public static Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> resultMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            // 排除 serialVersionUID 字段
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            try {
                resultMap.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                log.error("Failed to convert object to map", e);
            }
        }
        return resultMap;
    }


    /**
     * @param flag
     * @param obj
     * @return
     */
    public static String getDefaultStrIfTrue(boolean flag, String obj) {
        if (flag) {
            return obj;
        }
        return null;
    }

    /**
     * @param flag
     * @param obj
     * @return
     */
    public static Long getDefaultLongIfTrue(boolean flag, Long obj) {
        if (flag) {
            return obj;
        }
        return null;
    }

    /**
     * 提取对象中List<Long>
     *
     * @param obj
     * @return List<Long>
     */
    public static List<Long> extractListLong(Object obj) {
        List<Long> list = new ArrayList<>();
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            for (Object o : objList) {
                if (o instanceof Long l) {
                    list.add(l);
                }
            }
        } else {
            String string = obj.toString();
            list = strToList(string).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Object 转 Long
     */
    public static Long objectToLong(Object obj) {
        if (obj instanceof Long l) {
            return l;
        }
        if (ObjectUtil.isNotEmpty(obj)) {
            return Long.parseLong(obj.toString());
        }
        return 0L;
    }

    /**
     * String 转 List<String>
     *
     * @param str 字符串
     * @return List<String>
     */
    public static List<String> strToList(String str) {
        if (ObjectUtil.isNotEmpty(str)) {
            return Arrays.asList(str.split(","));
        }
        return new ArrayList<>();
    }

    public static Map<String, Map<String, Object>> compareObjects(Object obj1, Object obj2, Set<String> includeFields) {
        Map<String, Map<String, Object>> diffMap = new HashMap<>();
        compareFields(obj1, obj2, obj1.getClass(), diffMap);
        diffMap.entrySet().removeIf(entry -> !includeFields.contains(entry.getKey()));
        return diffMap;
    }

    public static Map<String, Map<String, Object>> compareObjects(Object obj1, Object obj2) {
        Map<String, Map<String, Object>> diffMap = new HashMap<>();
        compareFields(obj1, obj2, obj1.getClass(), diffMap);
        return diffMap;
    }

    private static void compareFields(Object obj1, Object obj2, Class<?> clazz, Map<String, Map<String, Object>> diffMap) {
        // 获取当前类的所有字段
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value1;
            Object value2;
            if (!field.getName().equals("name") && !field.getName().equals("description")) {
                try {
                    value1 = field.get(obj1);
                    value2 = field.get(obj2);
                } catch (Exception e) {
                    continue;
                }
            } else {
                try {
                    value1 = ReflectUtil.getProperty(obj1, field.getName());
                    value2 = ReflectUtil.getProperty(obj2, field.getName());
                } catch (Exception e) {
                    continue;
                }
            }

            // 比较字段值
            if (value1 == null && value2 != null || value1 != null && !value1.equals(value2)) {
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put(OLD_VAL, value1);
                valueMap.put(NEW_VAL, value2);
                diffMap.put(field.getName(), valueMap);
            }
        }

        // 递归获取父类的字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            compareFields(obj1, obj2, superClass, diffMap);
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


}


