package com.wdcloud.server.frame.interfaces;

import com.wdcloud.server.frame.exception.ResourceOpeartorUnsupportedException;

import java.util.Map;

/**
 * 查询接口
 *
 * @author csf
 * @Date 2015/7/21.
 */
public interface IMagicQueryComponent<T> {

    /**
     * 根据条件查询
     *
     * @param condition 条件（条件和前端约定)
     * @return 返回结果
     */
    default T search(Map<String, String> condition) {
        throw new ResourceOpeartorUnsupportedException();
    }

}