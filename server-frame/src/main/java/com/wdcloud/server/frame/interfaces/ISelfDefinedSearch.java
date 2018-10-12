package com.wdcloud.server.frame.interfaces;

import java.util.Map;

/**
 * 自定义查询 查询资源、查询名称
 *
 * @author csf
 * @Date 2015/7/24.
 */
public interface ISelfDefinedSearch<T> {
    /**
     * 根据条件查询
     *
     * @param condition 条件（条件和前端约定)
     * @return 返回结果
     */
    T search(Map<String, String> condition);
}
