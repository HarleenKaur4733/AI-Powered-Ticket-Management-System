package com.ticketsystem.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketStatus;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketRepository
                extends JpaRepository<Ticket, Long> {

        Page<Ticket> findByStatus(
                        TicketStatus status,
                        Pageable pageable);

        @Query("SELECT t FROM Ticket t JOIN FETCH t.assignedTo")
        List<Ticket> findAllWithAssignedUser();

}