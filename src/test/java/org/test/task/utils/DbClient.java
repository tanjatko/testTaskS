package org.test.task.utils;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbClient implements Closeable {
    private final Connection conn;
    private Statement stmt;
    private ResultSet rs;

    public DbClient(String connectionString) {
        try {
            this.conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(String command) throws SQLException {
        stmt = conn.createStatement();
        stmt.execute(command);
    }

    public List<Map<String, String>> getTestResults() throws SQLException {
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM test_results");

        List<Map<String, String>> listTestResults = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> map = new HashMap<>();
            for (int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
                 map.put(getColumnName(rs, i), rs.getString(i));

                 }
            listTestResults.add(map);
        }
        return listTestResults;
    }

    private String getColumnName(ResultSet rs, int index) throws SQLException {
        return rs.getMetaData().getColumnName(index);
    }

    public List<String> getColumnNames() throws SQLException {
       List<String> listOfColumnNames = new ArrayList<>();

        for (int i=1; i<=rs.getMetaData().getColumnCount(); i++)
        {
            listOfColumnNames.add(rs.getMetaData().getColumnName(i));
        }
        return listOfColumnNames;
    }

    @Override
    public void close() throws IOException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }
}
