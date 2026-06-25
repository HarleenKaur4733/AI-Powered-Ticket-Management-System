package com.ticketsystem.ticket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.dto.UpdateTicketRequest;
import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketStatus;
import com.ticketsystem.ticket.mapper.TicketMapper;
import com.ticketsystem.ticket.repository.TicketRepository;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.repository.UserRepository;
import com.ticketsystem.user.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserService userService;

    @Override
    public TicketResponse createTicket(CreateTicketRequest request) {

        Ticket ticket = ticketMapper.toEntity(request);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        // TODO: add created By after implementing authentication
        User assignedUser = userService.getUserById(request.getAssignedToUserId());
        ticket.setAssignedTo(assignedUser);
        Ticket newTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(newTicket);
    }

    @Override
    public TicketResponse getTicket(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        return ticketMapper.toResponse(ticket);
    }

    @Override
    public Page<TicketResponse> getAllTickets(Pageable pageable) {

        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toResponse);

    }

    @Override
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());
        User assignedUser = userService.getUserById(request.getAssignedToUserId());
        ticket.setAssignedTo(assignedUser);

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(updatedTicket);

    }

    @Override
    public TicketResponse deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ticketRepository.deleteById(id);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }

}
