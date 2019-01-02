package com.wdcloud.mq.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConvertMQO implements Serializable {

    private static final long serialVersionUID = -970725193464743191L;

    private String fileId;//文件ID
}
