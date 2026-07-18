package com.ticketsystem.ticket.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ticketsystem.ticket.entity.TicketPriority;
import com.ticketsystem.ticket.entity.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse implements Serializable {

    private Long id;

    private String title;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private Long createdById;
    private String createdByName;

    private Long assignedToId;
    private String assignedToName;

    private LocalDateTime createdAt;

}