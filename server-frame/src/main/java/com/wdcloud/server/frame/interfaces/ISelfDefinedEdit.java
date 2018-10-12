package com.wdcloud.server.frame.interfaces;

import com.wdcloud.server.frame.interfaces.info.DataEditInfo;
import com.wdcloud.server.frame.interfaces.info.LinkedInfo;

/**
 * 自定义查询 查询资源、查询名称
 *
 * @author csf
 * @Date 2015/7/24.
 */
public interface ISelfDefinedEdit {
    /**
     * 自定义更新方法
     *
     * @param dataEditInfo 数据更新时所需要信息
     * @return 返回结果
     */
    LinkedInfo edit(DataEditInfo dataEditInfo);
}
