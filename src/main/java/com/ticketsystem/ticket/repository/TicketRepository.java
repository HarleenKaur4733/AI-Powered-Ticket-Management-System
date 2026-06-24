package com.ticketsystem.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByStatus(
            TicketStatus status,
            Pageable pageable);
}