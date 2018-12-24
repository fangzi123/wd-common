package com.wdcloud.server.frame.interfaces;

import com.wdcloud.server.frame.exception.ResourceOpeartorUnsupportedException;
import com.wdcloud.server.frame.interfaces.info.LinkedInfo;

/**
 * 联动数据修改
 *
 * @author csf
 * @Date 2015/7/21.
 */
public interface IDataLinkedHandle {

    /**
     * 联动处理
     *
     * @param linkedInfo 联动消息
     */
    default LinkedInfo linkedHandle(LinkedInfo linkedInfo) { throw new ResourceOpeartorUnsupportedException(); }
}
