package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class TransactionBatchRunner {

    public static void main(String[] args) throws SQLException {

        long idToDelete = 8;

        String deleteFlightQuery = "DELETE FROM flight WHERE id = " + idToDelete;

        String deleteTicketQuery = "DELETE FROM ticket WHERE flight_id = " + idToDelete;

        Connection connection = null;
        Statement statement = null;

        try {

            connection = ConnectionManager.getConnection();

            // С помощью отмены автоматического коммита изменений можно избежать ситуаций,
            // когда ссылающийся ключ будет удалён, а тот, который планировалось удалить изначально нет.
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            // После создания выражения, вызываем метод addBatch(), добавляя подготовленные SQL-запросы.
            statement.addBatch(deleteTicketQuery);
            statement.addBatch(deleteFlightQuery);

            int[] transactionResult = statement.executeBatch();
            System.out.println(Arrays.toString(transactionResult));

            connection.commit();

        } catch (Exception e) {
            if (connection != null) { // Соединение может быть не инициализировано. Поэтому требуется проверка.
                connection.rollback();
            }
            throw e;

        } finally {
            if (connection != null) {
                connection.close();
            }

            if (statement != null) {
                statement.close();
            }
        }
    }

}
