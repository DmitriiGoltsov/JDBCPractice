package com.dvg.jdbc.starter;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;

import static com.dvg.jdbc.starter.util.ConnectionManager.openConnection;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {

        Class<Driver> driverClass = Driver.class;
        String sqlInquiry = """
                UPDATE info
                SET data = 'TestTest'
                WHERE id = 7
                RETURNING *;
                """;

        try (Connection psqlConnection = openConnection();
             Statement statement = psqlConnection.createStatement()) {
            System.out.println(psqlConnection.getSchema());
            System.out.println(psqlConnection.getTransactionIsolation());
            int result = statement.executeUpdate(sqlInquiry);
            System.out.println(result);
            System.out.println(statement.getUpdateCount());
        }

    }
}
