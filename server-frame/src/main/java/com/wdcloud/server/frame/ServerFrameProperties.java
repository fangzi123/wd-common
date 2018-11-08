package com.wdcloud.server.frame;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(ServerFrameProperties.PREFIX)
public class ServerFrameProperties {
    public static final String PREFIX = "server.frame";
    private boolean ex;
}