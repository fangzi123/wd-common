package com.wdcloud.utils;

import org.springframework.aop.framework.AopProxyUtils;

import java.lang.annotation.Annotation;

public class AnnotationUtils {
    /**
     * 获取注解信息
     *
     * @param cls             对象类型
     * @param annotationClass 注解类型
     * @param <T>             注解泛型
     * @return 注解信息
     */
    private static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {
        if (cls == null || annotationClass == null) {
            return null;
        }
        return cls.getAnnotation(annotationClass);
    }

    /**
     * 获取注解信息
     *
     * @param obj             对象
     * @param annotationClass 注解类型
     * @param <Obj>           传入对象类型
     * @param <T>             注解类型
     * @return 获取注解信息
     */
    public static <Obj, T extends Annotation> T getAnnotation(Obj obj, Class<T> annotationClass) {
        if (obj != null) {
            return getAnnotation(AopProxyUtils.ultimateTargetClass(obj), annotationClass);
        } else {
            return null;
        }
    }
}
