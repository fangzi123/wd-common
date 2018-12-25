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

    String dependFunctionName() default DEF_FUNCTION;

    OperateType operateType();

    public final static String DEF_FUNCTION = "";
}
