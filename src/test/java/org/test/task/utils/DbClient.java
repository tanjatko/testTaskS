package org.test.task.utils;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class DbClient implements Closeable {
    private final Connection conn;
    private  Statement stmt;
    private  ResultSet rs;

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

    public void showResultsFromDatabase() throws SQLException {
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM test_results");
        System.out.println("Output from database: ");
        System.out.println("|   id  |        startDate        |      testName       |    Status   |    Parameters  "    );
        while (rs.next()) {
            System.out.print("|   ");
            System.out.print(rs.getString(1));
            System.out.print("   |   ");
            System.out.print(rs.getString(2));
            System.out.print("   |   ");
            System.out.print(rs.getString(3));
            System.out.print("   |   ");
            System.out.print(rs.getString(4));
            System.out.print("   |   ");
            System.out.println(rs.getString(5));
        }
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
