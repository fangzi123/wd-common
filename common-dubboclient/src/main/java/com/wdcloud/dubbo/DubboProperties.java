package com.wdcloud.dubbo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Configuration properties for zk.
 *
 * @author andy
 */
@Validated
@ConfigurationProperties(prefix = "dubbo.registry")
public class DubboProperties {

    /**
     * dubbo application name
     */
    @NotNull
    private String name;
    /**
     * registration center address
     */
    @NotNull
    private String address;
    /**
     * dubbo service protocol
     * default zk
     */
    private String protocol = "zookeeper";
    /**
     * dubbo service protocol
     * default zk
     */
    private boolean check = false;
    /**
     * Login UserName of the service
     */
    private String userName;
    /**
     * Login password of the service.
     */
    private String password;
    /**
     * Connection timeout in milliseconds.
     */
    private int timeout = 0;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
