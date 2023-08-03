package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRunner {

    public static void main(String[] args) throws SQLException {

        String deleteFlightQuery = """
                DELETE FROM flight
                WHERE id = ?
                """;
        // Без запроса ниже удаление записи о вылете не произойдёт, так как на неё ссылается таблица билетов. Произойдёт
        // блокировка
        String deleteTicketQuery = """
                DELETE FROM ticket
                WHERE flight_id = ?
                """;

        long idToDelete = 8L;

        Connection connection = null;
        PreparedStatement deleteFlightStatement = null;
        PreparedStatement deleteTicketStatement = null;

        try {

            connection = ConnectionManager.getConnection();
            deleteFlightStatement = connection.prepareStatement(deleteFlightQuery);
            deleteTicketStatement = connection.prepareStatement(deleteTicketQuery);

            // С помощью отмены автоматического коммита изменений можно избежать ситуаций,
            // когда ссылающийся ключ будет удалён, а тот, который планировалось удалить изначально нет.

            connection.setAutoCommit(false);

            deleteFlightStatement.setLong(1, idToDelete);
            deleteTicketStatement.setLong(1, idToDelete);

            // Сначала удаляем зависимую строку, затем уже удаляем изначальную, т.е. ту, на которую ссылался тикет
            deleteTicketStatement.executeUpdate();

            // Специальный код, чтобы продемонстрировать как работает ролбэк
            if (true) {
                throw new RuntimeException("Opanjki");
            }
            deleteFlightStatement.executeUpdate();

            // Только после этой строки будет произведён коммит, и изменения вступят в силу:
            // транзакция будет зафиксирована.
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

            if (deleteFlightStatement != null) {
                deleteFlightStatement.close();
            }

            if (deleteTicketStatement != null) {
                deleteTicketStatement.close();
            }
        }
    }
}
