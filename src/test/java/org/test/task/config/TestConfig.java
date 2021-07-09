package org.test.task.config;

import java.util.Properties;

public class TestConfig {
    private final String url;
    private final int port;
    private final String password;
    private final String userName;
    private final String pathdriver;
    private final String mysqlconnectionstring;

    public TestConfig(Properties properties) {
        this.url = properties.getProperty("url");
        this.port = Integer.parseInt(properties.getProperty("mysql_port"));
        this.password = properties.getProperty("mysql_password");
        this.userName = properties.getProperty("mysql_username");
        this.pathdriver = properties.getProperty("path_to_driver");
        this.mysqlconnectionstring = properties.getProperty("mysql_connection_string");
    }

    public String getMysqlconnectionstring() { return mysqlconnectionstring; }

    public String getPathDriver() { return pathdriver; }

    public String getUrl() { return url; }

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
