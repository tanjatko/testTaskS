package org.test.task;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.test.task.config.TestConfig;
import org.test.task.utils.DbClient;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;

public class BaseTest {
    protected WebDriver driver;
    protected EmbeddedMysql mysql;
    protected TestConfig testConfig;

    @BeforeSuite
    public void init() throws IOException {
            try (FileInputStream fileInput = new FileInputStream("test.properties")) {
                Properties properties = new Properties();
                properties.load(fileInput);
                testConfig = new TestConfig(properties);
            }

        System.setProperty("webdriver.chrome.driver", testConfig.getPathDriver());
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        MysqldConfig config = aMysqldConfig(v5_6_23)
                .withUser(testConfig.getUserName(), testConfig.getPassword())
                .withPort(testConfig.getPort())
                .build();
        mysql = anEmbeddedMysql(config)
                .addSchema("aschema", classPathScript("db/db_init.sql"))
                .start();
    }

    @AfterSuite
    public void tearDown() throws SQLException, IOException {
        driver.close();
        showResultsListFromDb();
        mysql.stop();
    }

    private void showResultsListFromDb() throws SQLException, IOException {
        DbClient dbclient = new DbClient(testConfig.getMysqlconnectionstring() + testConfig.getPort() + "/aschema?" +
                "user=" + testConfig.getUserName() + "&password=" + testConfig.getPassword());
        List<Map<String, String>> listOfResults = dbclient.getTestResults();
        List<String> listOfColumnNames  = dbclient.getColumnNames();

        for (String columnName: listOfColumnNames) {
            System.out.print("|     ");
            System.out.print(columnName + "     ");
        }

        for(Map<String, String> listItem : listOfResults){
            System.out.println();
            for (String columnName: listOfColumnNames) {
                System.out.print("|     ");
                System.out.print(listItem.get(columnName) + "       ");
            }
        }
        dbclient.close();
    }

}
