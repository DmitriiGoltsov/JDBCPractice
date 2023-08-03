package com.dvg.jdbc.starter;

import com.dvg.jdbc.starter.dao.TicketDao;
import com.dvg.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;

public class DaoRunner {

    public static void main(String[] args) {

        TicketDao ticketDao = TicketDao.getInstance();

        boolean isDeleted = ticketDao.delete(59L);

        System.out.println(isDeleted);

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

}
