package org.test.task;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.test.task.utils.DbClient;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;

public class BaseTest {
    WebDriver driver ;
    EmbeddedMysql mysqld ;
    DbClient dbclient;
    String url = "";
    int port;
    String password = "";
    String userName = "";

    @BeforeSuite
    public void init() throws IOException {
        File file = new File("test.properties");
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();

        url = properties.getProperty("url");
        port = Integer.parseInt(properties.getProperty("port"));
        password = properties.getProperty("password");
        userName = properties.getProperty("userName");

        System.setProperty("webdriver.chrome.driver", "/Users/botanjatko/Downloads/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        MysqldConfig config = aMysqldConfig(v5_6_23)
                .withUser(userName, password)
                .withPort(port)
                .build();

        mysqld = anEmbeddedMysql(config)
                .addSchema("aschema", classPathScript("db/db_init.sql"))
                .start();

        dbclient = new DbClient("jdbc:mysql://localhost:"+ port+ "/aschema?" +
                "user="+ userName+ "&password=" + password);
    }

    @AfterSuite
    public void tearDown() throws SQLException, IOException {
        driver.close();
        dbclient.showResultsFromDatabase();
        dbclient.close();
        mysqld.stop();
    }
}
