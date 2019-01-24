package com.wdcloud.mq.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * mq配置
 *
 * @author Andy
 * @date 17/4/17
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class MqConfig {

    private String username = "guest";

    private String password = "guest";

    private String virtualHost = "/";

    /**
     * 和rabbitmq建立连接的超时时间
     */
    private int connectionTimeout = 0;

    @NotNull
    private String addresses;

    /**
     * 事件消息处理线程数，默认是 CPU核数 * 2
     */
    private int eventMsgProcessNum = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 每次消费消息的预取值
     */
    private int prefetchSize = 1;

    public void setPrefetchSize(int prefetchSize) {
        this.prefetchSize = prefetchSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getEventMsgProcessNum() {
        return eventMsgProcessNum;
    }

    public void setEventMsgProcessNum(int eventMsgProcessNum) {
        this.eventMsgProcessNum = eventMsgProcessNum;
    }

    public int getPrefetchSize() {
        return prefetchSize;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "MqConfig{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", virtualHost='" + virtualHost + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                ", addresses='" + addresses + '\'' +
                ", eventMsgProcessNum=" + eventMsgProcessNum +
                ", prefetchSize=" + prefetchSize +
                '}';
    }
}
