package org.test.task.config;

import java.util.Properties;

public class TestConfig {
    private final String url;
    private final int port;
    private final String password;
    private final String userName;

    public TestConfig(Properties properties) {
        this.url = properties.getProperty("url");
        this.port = Integer.parseInt(properties.getProperty("mysql_port"));
        this.password = properties.getProperty("mysql_password");
        this.userName = properties.getProperty("mysql_username");
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
