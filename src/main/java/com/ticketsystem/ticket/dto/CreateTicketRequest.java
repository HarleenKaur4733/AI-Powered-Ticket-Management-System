package com.ticketsystem.ticket.dto;

import com.ticketsystem.ticket.entity.TicketPriority;

import jakarta.validation.constraints.NotBlank;
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
public class CreateTicketRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private TicketPriority priority;

    private Long assignedToUserId;

}