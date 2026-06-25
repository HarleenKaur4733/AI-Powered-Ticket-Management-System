package com.ticketsystem.ticket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.dto.UpdateTicketRequest;
import com.ticketsystem.ticket.entity.Ticket;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    TicketResponse getTicket(Long id);

    Ticket getTicketById(Long id);

    Page<TicketResponse> getAllTickets(Pageable pageable);

    TicketResponse updateTicket(Long id, UpdateTicketRequest request);

    void deleteTicket(Long id);

}