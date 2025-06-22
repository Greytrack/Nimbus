package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author jiangkun@airedgesoft.com
 */
@UtilityClass
@Slf4j
public class ReflectUtil extends ReflectionUtils {

    /**
     * 获取 Bean 的所有 get方法
     *
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * 获取 Bean 的所有 set方法
     *
     * @param type 类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
        return getPropertiesHelper(type, false, true);
    }

    /**
     * 获取 Bean 的所有 PropertyDescriptor
     *
     * @param type  类
     * @param read  读取方法
     * @param write 写方法
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    if ((read && pd.getReadMethod() != null) || (write && pd.getWriteMethod() != null)) {
                        properties.add(pd);
                    }
                }
                return properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }

    /**
     * 获取 bean 的属性信息
     *
     * @param propertyType 类型
     * @param propertyName 属性名
     * @return {Property}
     */
    @Nullable
    public static Property getProperty(Class<?> propertyType, String propertyName) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(propertyType, propertyName);
        if (propertyDescriptor == null) {
            return null;
        }
        return ReflectUtil.getProperty(propertyType, propertyDescriptor, propertyName);
    }

    /**
     * 获取 bean 的属性信息
     *
     * @param propertyType       类型
     * @param propertyDescriptor PropertyDescriptor
     * @param propertyName       属性名
     * @return {Property}
     */
    public static Property getProperty(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        return new Property(propertyType, readMethod, writeMethod, propertyName);
    }

    /**
     * 获取 bean 的属性信息
     *
     * @param propertyType 类型
     * @param propertyName 属性名
     * @return {Property}
     */
    @Nullable
    public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, String propertyName) {
        Property property = ReflectUtil.getProperty(propertyType, propertyName);
        if (property == null) {
            return null;
        }
        return new TypeDescriptor(property);
    }

    /**
     * 获取 类属性信息
     *
     * @param propertyType       类型
     * @param propertyDescriptor PropertyDescriptor
     * @param propertyName       属性名
     * @return {Property}
     */
    public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
        return new TypeDescriptor(property);
    }

    /**
     * 获取 类属性
     *
     * @param clazz     类信息
     * @param fieldName 属性名
     * @return Field
     */
    @Nullable
    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取 所有 field 属性上的注解
     *
     * @param clazz           类
     * @param fieldName       属性名
     * @param annotationClass 注解
     * @param <T>             注解泛型
     * @return 注解
     */
    @Nullable
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        Field field = ReflectUtil.getField(clazz, fieldName);
        if (field == null) {
            return null;
        }
        return field.getAnnotation(annotationClass);
    }

    /**
     * 获取属性
     *
     * @param bean 对象
     * @param name 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String name) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(name, bean.getClass());
            Method readMethod = descriptor.getReadMethod();
            return readMethod.invoke(bean);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            log.error("获取属性失败", e);
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 设置属性
     *
     * @param bean  对象
     * @param name  属性名
     * @param value 值
     * @throws IllegalAccessException    异常
     * @throws InvocationTargetException 异常
     * @throws IntrospectionException    异常
     */
    public static void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        PropertyDescriptor descriptor = new PropertyDescriptor(name, bean.getClass());
        Method writeMethod = descriptor.getWriteMethod();
        writeMethod.invoke(bean, value);
    }

    /**
     * 获取类的所有常量
     * @param clazz 类
     * @return 常量
     */
    public static Map<String, Object> parseConstants(Class<?> clazz) {
        Map<String, Object> constants = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                try {
                    constants.put(field.getName(), field.get(null));
                } catch (IllegalAccessException e) {
                    log.error("获取常量失败", e);
                    throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return constants;
    }
}
