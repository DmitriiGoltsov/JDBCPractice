package com.dvg.jdbc.starter;

import org.postgresql.Driver;

import java.sql.*;

import static com.dvg.jdbc.starter.util.ConnectionManager.openConnection;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {

        Class<Driver> driverClass = Driver.class;
        String sqlInquiry = """
                SELECT *
                FROM ticket;
                """;

        try (Connection psqlConnection = openConnection();
             Statement statement = psqlConnection.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            System.out.println(psqlConnection.getSchema());
            System.out.println(psqlConnection.getTransactionIsolation());

            ResultSet result = statement.executeQuery(sqlInquiry);

            while (result.next()) {
                System.out.println(result.getLong("id"));
                System.out.println(result.getString("passenger_no"));
                System.out.println(result.getBigDecimal("cost"));
                /* Следующие 2 строки демонстрируют возможность внутри цикла обновлять данные и
                итерироваться в другую сторону соответственно

                result.updateLong(2, 100L);
                result.previous();

                */
                System.out.println("-------------------");
            }
        }

    }
}
