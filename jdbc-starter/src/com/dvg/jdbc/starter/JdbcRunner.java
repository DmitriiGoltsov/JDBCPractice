package com.dvg.jdbc.starter;

import org.postgresql.Driver;

import java.sql.*;

import static com.dvg.jdbc.starter.util.ConnectionManager.getConnection;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {

        Class<Driver> driverClass = Driver.class;
        String sqlInquiry = """
                INSERT INTO info (data)
                VALUES 
                ('autogenerated');
                """;

        try (Connection psqlConnection = getConnection();
             Statement statement = psqlConnection.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            System.out.println(psqlConnection.getSchema());
            System.out.println(psqlConnection.getTransactionIsolation());

            int result = statement.executeUpdate(sqlInquiry, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                // In Java int is an analogue of SQL type SERIAL.
                // You can use either number or String for defining a stroke or a particular element you want to get
                //int generatedId = generatedKeys.getInt(1);
                int generatedId = generatedKeys.getInt("id");
                System.out.println(generatedId);
            }
        }

    }
}
