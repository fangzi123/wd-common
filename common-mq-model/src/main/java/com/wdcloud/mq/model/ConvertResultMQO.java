package com.wdcloud.mq.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConvertResultMQO implements Serializable {

    private static final long serialVersionUID = -1521887780668090827L;

    private String fileId;//文件ID
    private String originName;
    private Long fileSize;
    private String fileType;
    /**
     * 转换状态 -1:失败 0 1:成功
     */
    private Integer convertStatus;
    private String convertType;//pdf mp4
    private String convertResult;
    private String convertErrorMsg;
}
