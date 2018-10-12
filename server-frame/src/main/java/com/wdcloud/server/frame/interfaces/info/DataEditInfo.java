package com.wdcloud.server.frame.interfaces.info;

/**
 * 数据修改传入信息
 *
 * @author csf
 * @Date 2015/7/24.
 */
public class DataEditInfo {
    /**
     扩展参数
     */
    public final String extendedParams;
    /**
    用户ID
     */
    public final String userId;
    /**
     * 修改对象信息（1、增加、修改时，传入JSON 2、删除时，为ID）
     */
    public final String beanJson;

    public DataEditInfo(String beanJson) {
        this(null, beanJson);
    }

    public DataEditInfo(String userId, String beanJson) {
        this.extendedParams = null;
        this.beanJson = beanJson;
        this.userId=userId;
    }

    public DataEditInfo(String beanJson, String userId,String extendedParams) {
        this.extendedParams = extendedParams;
        this.beanJson = beanJson;
        this.userId=userId;
    }
}
