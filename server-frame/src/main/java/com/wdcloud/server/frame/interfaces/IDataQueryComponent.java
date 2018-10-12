package com.wdcloud.server.frame.interfaces;

import com.wdcloud.server.frame.interfaces.info.PageQueryResult;

import java.util.List;
import java.util.Map;

/**
 * 查询接口
 *
 * @author csf
 * @Date 2015/7/21.
 */
public interface IDataQueryComponent<T> {

    /**
     * 列表查询
     *
     * @param param 条件（条件和前端约定)
     * @return 查询列表
     */
    List<? extends T> list(Map<String, String> param);

    /**
     * 分页查询
     *
     * @param param     条件（由request中提取参数，直接可转换成map)
     * @param pageIndex 第几页
     * @param pageSize  每页显示多少条
     * @return 分页信息和列表
     */
    PageQueryResult<? extends T> pageList(Map<String, String> param, int pageIndex, int pageSize);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 返回对象
     */
    T find(String id);

}