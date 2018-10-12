package com.wdcloud.server.frame.interfaces;


import com.wdcloud.server.frame.interfaces.info.DataEditInfo;
import com.wdcloud.server.frame.interfaces.info.LinkedInfo;

/**
 * 数据修改接口
 *
 * @author csf
 * @Date 2015/7/21.
 */
public interface IDataEditComponent {

    /**
     * 增加
     *
     * @param dataEditInfo 数据增加时所需要信息
     * @return 返回ID
     */
    LinkedInfo add(DataEditInfo dataEditInfo);

    /**
     * 修改/更新
     *
     * @param dataEditInfo 数据修改时所需要信息
     */
    LinkedInfo update(DataEditInfo dataEditInfo);

    /**
     * 根据ID删除
     *
     * @param dataEditInfo 数据删除时所需要信息
     */
    LinkedInfo delete(DataEditInfo dataEditInfo);
}
