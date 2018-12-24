package com.wdcloud.server.frame.interfaces;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 联动注解注解
 *
 * @author csf
 * @Date 2015/7/24.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
@Documented
public @interface LinkedHandler {

    String dependResourceName();

    OperateType operateType();
}
