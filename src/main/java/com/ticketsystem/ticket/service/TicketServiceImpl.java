package com.ticketsystem.ticket.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ticketsystem.exception.ResourceNotFoundException;
import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketDashboardResponse;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.dto.UpdateTicketRequest;
import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketStatus;
import com.ticketsystem.ticket.mapper.TicketMapper;
import com.ticketsystem.ticket.repository.TicketRepository;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

    @Cacheable(value = "tickets", key = "#id")
    @Override
    public TicketResponse getTicket(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        return ticketMapper.toResponse(ticket);
    }

    @Override
    public Page<TicketResponse> getAllTickets(Pageable pageable) {

        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toResponse);

    }

    @CacheEvict(value = "tickets", key = "#id")
    @Override
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (request.getPriority() != null) {
            ticket.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
        }
        if (request.getAssignedToUserId() != null) {
            User assignedUser = userService.getUserById(request.getAssignedToUserId());
            ticket.setAssignedTo(assignedUser);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);

        return ticketMapper.toResponse(updatedTicket);

    }

    @CacheEvict(value = "tickets", key = "#id")
    @Override
    public void deleteTicket(Long id) {
        ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticketRepository.deleteById(id);
    }

    @Cacheable(value = "tickets", key = "#id")
    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    @Override
    public List<TicketDashboardResponse> getDashboardTickets() {

        List<Ticket> tickets = ticketRepository.findAllWithAssignedUser();

        return tickets.stream()
                .map(ticket -> TicketDashboardResponse.builder()
                        .id(ticket.getId())
                        .title(ticket.getTitle())
                        .assignedTo(ticket.getAssignedTo().getName())
                        .build())
                .toList();
    }

}
