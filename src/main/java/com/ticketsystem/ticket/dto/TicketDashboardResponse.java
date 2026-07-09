package com.ticketsystem.ticket.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketDashboardResponse {

    private Long id;

    private String title;

    private String assignedTo;
}