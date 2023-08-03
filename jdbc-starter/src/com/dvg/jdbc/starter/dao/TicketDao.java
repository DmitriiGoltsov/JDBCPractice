package com.dvg.jdbc.starter.dao;

import com.dvg.jdbc.starter.entity.TicketEntity;
import com.dvg.jdbc.starter.exception.DaoException;
import com.dvg.jdbc.starter.util.ConnectionManager;

import java.sql.*;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_QUERY = "DELETE FROM ticket WHERE id = ?";
    private static final String INSERT_QUERY = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES 
                (?, ?, ?, ?, ?)
            """;

    private TicketDao(){
    }

    public TicketEntity save(TicketEntity ticket) {

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                ticket.setId(resultSet.getLong("id"));
            }

            return ticket;

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public boolean delete(Long id) {

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

}
