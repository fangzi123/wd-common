package com.wdcloud.mq.handler;

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
public @interface MQHandler {

    /**
     * 监听交换区
     */
    String exchangeName();

    /**
     * 监听队列
     */
    String queueName() default "";

    /**
     * isFanout 是否是广播监听
     */
    boolean isFanout() default false;

    /**
     * 资源描述信息（简介）
     */
    String description() default "未描述";

}
