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
                CREATE TABLE IF NOT EXISTS info (
                    id SERIAL PRIMARY KEY ,
                    data TEXT NOT NULL 
                );
                """;

        try (Connection psqlConnection = openConnection();
             Statement statement = psqlConnection.createStatement()) {
            System.out.println(psqlConnection.getSchema());
            System.out.println(psqlConnection.getTransactionIsolation());
            boolean result = statement.execute(sqlInquiry);
            System.out.println(result);
        }

    }
}
