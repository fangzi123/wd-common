package com.wdcloud.server.frame.interfaces;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 资源类别注解
 *
 * @author csf
 * @Date 2015/7/24.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface ResourceInfo {

    /**
     * 资源名称
     */
    String name();

    /**
     * 资源描述信息（简介）
     */
    String description() default "未描述";

}
