package com.cjc.nimbus.utils;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类操作工具
 *
 * @author jiangkun@airedgesoft.com
 */
@Slf4j
public class ClassUtil extends ClassUtils {

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取Annotation
     *
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        Class<?> targetClass = method.getDeclaringClass();
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 先找方法，再找方法上的类
        A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    /**
     * 获取Annotation
     *
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法，再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }


    /**
     * 判断是否有注解 Annotation
     *
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {boolean}
     */
    public static <A extends Annotation> boolean isAnnotated(Method method, Class<A> annotationType) {
        // 先找方法，再找方法上的类
        boolean isMethodAnnotated = AnnotatedElementUtils.isAnnotated(method, annotationType);
        if (isMethodAnnotated) {
            return true;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        Class<?> targetClass = method.getDeclaringClass();
        return AnnotatedElementUtils.isAnnotated(targetClass, annotationType);
    }

    /**
     * 实例化对象.
     *
     * @param clazzName 类名
     * @param <T>       类型
     * @return 实例
     * @since 3.3.2
     */
    public static <T> T newInstance(String clazzName) {
        return (T) newInstance(toClassConfident(clazzName));
    }

    /**
     * <p>
     * 根据指定的 class ， 实例化一个对象，根据构造参数来实例化
     * </p>
     * <p>
     * 在 java9 及其之后的版本 Class.newInstance() 方法已被废弃
     * </p>
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类型，由输入类型决定
     * @return 返回新的实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("实例化对象时出现错误,请尝试给 {} 添加无参的构造方法", clazz.getName(), e);
            throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * <p>
     * 请仅在确定类存在的情况下调用该方法
     * </p>
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     */
    public static Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name, false, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ex) {
                log.error("找不到指定的class[{}]！请仅在明确确定会有 class 的时候，调用该方法", name, e);
                throw new AppException(GlobalResultCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * 根据类的class实例化对象
     *
     * @param clazz 类的class
     * @return T
     */
    public static <T> T instantiate(Class<T> clazz) {
        if (clazz.isInterface()) {
            log.error("所传递的class类型参数为接口，无法实例化");
            return null;
        }
        try {
            return newInstance(clazz);
        } catch (Exception ex) {
            log.error("检查传递的class类型参数是否为抽象类?", ex.getCause());
        }
        return null;
    }

}
