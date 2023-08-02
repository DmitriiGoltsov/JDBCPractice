package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JDBCRunnerForSQLInjection {

    public static void main(String[] args) throws SQLException {

//        long flightId = 2L;
//        System.out.println(getTicketsByFlightId(flightId));

        /*List<Long> result = getFlightsBetween(LocalDate.of(2020, 10, 1).atStartOfDay(),
                LocalDateTime.now());

        System.out.println(result);
*/
        checkMetaData();
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {

        List<Long> result = new ArrayList<>();

        String query = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setFetchSize(3);

            System.out.println(preparedStatement);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        }

        return result;
    }

    private static List<Long> getTicketsByFlightId(long flightId) throws SQLException {

        List<Long> idList = new ArrayList<>();

        String query = """
                SELECT id
                FROM ticket
                WHERE flight_id = ?
                """;

        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, flightId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // idList.add(resultSet.getLong("id")); хотя подобный код и рабочий, его использование нежелательно,
                // ибо, если речь не об АйДи, то высок риск получения null в примитивный тип. Лучше использовать код ниже.
                idList.add(resultSet.getObject("id", Long.class));
            }

        }
        return idList;
    }

    private static void checkMetaData() throws SQLException {
        try (Connection connection = ConnectionManager.openConnection()) {

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();

            while (catalogs.next()) {
                String catalog = catalogs.getString(1);// Лучше обращаться по конкретному имени каталога
                ResultSet schemas = metaData.getSchemas();

                while (schemas.next()) {
                    String schema = schemas.getString("TABLE_SCHEM");
                    ResultSet tables = metaData.getTables(null, null,
                            "%", new String[] {"TABLE"});
                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }
            }
        }

    }
}
