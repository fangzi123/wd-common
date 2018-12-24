package com.wdcloud.server.frame.interfaces;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义查询方法注解
 *
 * @author csf
 * @Date 2015/7/24.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface SelfDefinedFunction {
    /**
     * 方法名称
     */
    String resourceName();

    /**
     * 方法名称
     */
    String functionName() default "";
}
