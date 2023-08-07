package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.dao.TicketDao;
import com.dvg.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {

        TicketDao ticketDao = TicketDao.getInstance();

        System.out.println(ticketDao.findALl());

/*
        boolean isDeleted = ticketDao.delete(59L);

        System.out.println(isDeleted);*/

        /*TicketDao ticketDao = TicketDao.getInstance();

        TicketEntity ticket = new TicketEntity();
        ticket.setPassengerNo("1234567");
        ticket.setPassengerName("Alex");
        ticket.setFlightId(3L);
        ticket.setSeatNo("A356");
        ticket.setCost(BigDecimal.TEN);

        TicketEntity savedTicket = ticketDao.save(ticket);

        System.out.println(savedTicket.toString());
*/
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getInstance();

        Optional<TicketEntity> maybeTicketToUpdate = ticketDao.findById(2L);
        System.out.println(maybeTicketToUpdate);

        maybeTicketToUpdate.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
        });
    }

}
