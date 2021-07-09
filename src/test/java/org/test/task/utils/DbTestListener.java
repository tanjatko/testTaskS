package org.test.task.utils;

import org.test.task.config.TestConfig;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class DbTestListener extends TestListenerAdapter {
    int id = 0;
    protected TestConfig testConfig;

    @Override
    public void onFinish(ITestContext testContext) {
        try {
            try (FileInputStream fileInput = new FileInputStream("test.properties")) {
                Properties properties = new Properties();
                properties.load(fileInput);
                testConfig = new TestConfig(properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DbClient dbclient = new DbClient(testConfig.getMysqlconnectionstring() + testConfig.getPort() + "/aschema?" +
                "user=" + testConfig.getUserName() + "&password=" + testConfig.getPassword());
        addToDatabase(testContext.getPassedTests().getAllResults() , dbclient);
        addToDatabase(testContext.getFailedTests().getAllResults(), dbclient);
        addToDatabase(testContext.getSkippedTests().getAllResults(), dbclient);
        addToDatabase(testContext.getFailedButWithinSuccessPercentageTests().getAllResults(), dbclient);
    }

    private void addToDatabase(Set<ITestResult> testResults, DbClient dbclient)
    {
        for (ITestResult tr : testResults
        ) {
            Date date = new Date(tr.getStartMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(date);
            try {
                dbclient.execute(" INSERT INTO test_results values(" + id + ",\"" + currentTime
                        + "\", \"" + tr.getName() + "\", \"" + convertStatusCode(tr.getStatus()) + "\", \"" +
                        Arrays.toString(tr.getParameters()) + "\");");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private String convertStatusCode(int code) {
        String answer;
        switch (code) {
            case 1:
                answer = "Success";
                break;
            case 2:
                answer = "Failure";
                break;
            case 3:
                answer = "Skip";
                break;
            case 4:
                answer = "Success_percentage_failure";
                break;
            default:
                answer = "Started";
                break;
        }
        return answer;
    }

}

