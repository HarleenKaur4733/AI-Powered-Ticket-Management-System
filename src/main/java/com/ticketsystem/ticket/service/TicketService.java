package com.ticketsystem.ticket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.dto.UpdateTicketRequest;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    TicketResponse getTicket(Long id);

    Page<TicketResponse> getAllTickets(Pageable pageable);

    TicketResponse updateTicket(Long id, UpdateTicketRequest request);

    TicketResponse deleteTicket(Long id);

}