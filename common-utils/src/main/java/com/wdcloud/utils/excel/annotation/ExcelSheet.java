package com.wdcloud.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/12/15 10:08
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
    /**
     * 表格标题
     */
    String title();

    boolean show() default true;

    String sheet() default "sheet";
}
