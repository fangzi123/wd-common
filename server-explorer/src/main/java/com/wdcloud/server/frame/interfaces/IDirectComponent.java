package com.wdcloud.server.frame.interfaces;

import com.wdcloud.server.frame.exception.ResourceOpeartorUnsupportedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.*;

/**
 * 直接调用组件
 */
public interface IDirectComponent<T> {

    default void handle(HttpServletRequest request, HttpServletResponse response) {
        throw new ResourceOpeartorUnsupportedException();
    }

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Component
    @Documented
    @interface DirectHandler {
        /**
         * 资源名称
         */
        String resource();

        String function();

        /**
         * 描述
         */
        String description() default "";
    }
}
