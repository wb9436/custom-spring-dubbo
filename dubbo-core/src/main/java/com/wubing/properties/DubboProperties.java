package com.wubing.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义Dubbo配置文件
 *
 * @author: WB
 * @version: v1.0
 */
@ConfigurationProperties(prefix = "dubbo", ignoreInvalidFields = true)
public class DubboProperties {
    /**
     * Netty Server HostName
     */
    private String host = "127.0.0.1";
    /**
     * Netty Server Port
     */
    private Integer port = 20880;
    /**
     * Redis Register HostName
     */
    private String registerHost = "127.0.0.1";

    /**
     * Redis Register Port
     */
    private int registerPort = 6379;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRegisterHost() {
        return registerHost;
    }

    public void setRegisterHost(String registerHost) {
        this.registerHost = registerHost;
    }

    public int getRegisterPort() {
        return registerPort;
    }

    public void setRegisterPort(int registerPort) {
        this.registerPort = registerPort;
    }
}
