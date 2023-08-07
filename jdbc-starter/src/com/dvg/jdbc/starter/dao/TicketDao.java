package com.dvg.jdbc.starter.dao;

import com.dvg.jdbc.starter.dto.TicketFilter;
import com.dvg.jdbc.starter.entity.TicketEntity;
import com.dvg.jdbc.starter.exception.DaoException;
import com.dvg.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_QUERY = "DELETE FROM ticket WHERE id = ?";
    private static final String INSERT_QUERY = """
            INSERT INTO ticket (passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES 
                (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_QUERY  = """
            UPDATE ticket
            SET passenger_no = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_no = ?,
                cost = ?
            WHERE id = ?            
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT 
                    id, 
                    passenger_no, 
                    passenger_name, 
                    flight_id, 
                    seat_no, 
                    cost
            FROM ticket
            """;

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE id = ?
            """;

    private TicketDao(){
    }

    public List<TicketEntity> findAll(TicketFilter filter) {

        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.seatNo() != null) {
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" + filter.seatNo() + "%");
        }
        if (filter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            parameters.add(filter.passengerName());
        }

        parameters.add(filter.limit());
        parameters.add(filter.offset());

        var where = whereSql.stream()
                .collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        var sql = FIND_ALL_QUERY + where;

        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(preparedStatement);
            var resultSet = preparedStatement.executeQuery();
            List<TicketEntity> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicketEntity(resultSet));
            }
            return tickets;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<TicketEntity> findALl() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<TicketEntity> resultList = new ArrayList<>();

            while (resultSet.next()) {
                resultList.add(buildTicketEntity(resultSet));
            }

            return resultList;

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

        public Optional<TicketEntity> findById(Long id) {

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            TicketEntity ticketResult = null;
            if (resultSet.next()) {
                ticketResult = buildTicketEntity(resultSet);
            }

            return Optional.ofNullable(ticketResult);

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private static TicketEntity buildTicketEntity(ResultSet resultSet) throws SQLException {
        TicketEntity ticketResult;
        ticketResult = new TicketEntity(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                resultSet.getLong("flight_id"),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
        return ticketResult;
    }

    public void update(TicketEntity ticket) {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(UPDATE_QUERY)) {

            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
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
