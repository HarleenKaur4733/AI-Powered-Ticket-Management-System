package com.ticketsystem.ticket.mapper;

import org.springframework.stereotype.Component;

import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.entity.Ticket;

@Component
public class TicketMapper {

    public Ticket toEntity(CreateTicketRequest request) {

        if (request == null) {
            return null;
        }

        return Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .build();
    }

    public TicketResponse toResponse(Ticket ticket) {

        if (ticket == null) {
            return null;
        }

        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdAt(ticket.getCreatedAt())
                .createdById(
                        ticket.getCreatedBy() != null
                                ? ticket.getCreatedBy().getId()
                                : null)
                .createdByName(
                        ticket.getCreatedBy() != null
                                ? ticket.getCreatedBy().getName()
                                : null)
                .assignedToId(ticket.getAssignedTo() != null
                        ? ticket.getAssignedTo().getId()
                        : null)
                .assignedToName(ticket.getAssignedTo() != null
                        ? ticket.getAssignedTo().getName()
                        : null)
                .build();
    }
}