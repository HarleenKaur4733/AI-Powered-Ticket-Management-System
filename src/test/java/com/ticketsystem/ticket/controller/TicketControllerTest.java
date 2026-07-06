package com.ticketsystem.ticket.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketsystem.security.CustomUserDetailsService;
import com.ticketsystem.security.JwtAuthenticationFilter;
import com.ticketsystem.security.JwtService;
import com.ticketsystem.ticket.dto.*;
import com.ticketsystem.ticket.entity.*;
import com.ticketsystem.ticket.service.TicketService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

        @Autowired
        MockMvc mockMvc;
        @Autowired
        ObjectMapper objectMapper;

        @MockBean
        TicketService ticketService;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private CustomUserDetailsService customUserDetailsService;

        @MockBean
        private JwtService jwtService;

        @Test
        void shouldCreateTicketSuccessfully() throws Exception {
                CreateTicketRequest request = CreateTicketRequest.builder()
                                .title("Login Issue")
                                .description("Cannot login")
                                .assignedToUserId(1L)
                                .build();

                TicketResponse response = TicketResponse.builder()
                                .id(1L)
                                .title("Login Issue")
                                .description("Cannot login")
                                .status(TicketStatus.OPEN)
                                .build();

                when(ticketService.createTicket(any())).thenReturn(response);

                mockMvc.perform(post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Login Issue"));
        }

        @Test
        void shouldGetTicketSuccessfully() throws Exception {
                TicketResponse response = TicketResponse.builder()
                                .id(1L).title("Login Issue").status(TicketStatus.OPEN).build();

                when(ticketService.getTicket(1L)).thenReturn(response);

                mockMvc.perform(get("/api/tickets/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.status").value("OPEN"));
        }

        @Test
        void shouldReturnAllTickets() throws Exception {
                TicketResponse response = TicketResponse.builder()
                                .id(1L).title("Login Issue").build();

                when(ticketService.getAllTickets(any()))
                                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

                mockMvc.perform(get("/api/tickets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1));
        }

        @Test
        void shouldUpdateTicketSuccessfully() throws Exception {
                UpdateTicketRequest request = UpdateTicketRequest.builder()
                                .status(TicketStatus.IN_PROGRESS)
                                .priority(TicketPriority.HIGH)
                                .assignedToUserId(2L)
                                .build();

                TicketResponse response = TicketResponse.builder()
                                .id(1L).status(TicketStatus.IN_PROGRESS).build();

                when(ticketService.updateTicket(any(Long.class), any(UpdateTicketRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(patch("/api/tickets/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }

        @Test
        void shouldDeleteTicketSuccessfully() throws Exception {
                doNothing().when(ticketService).deleteTicket(1L);

                mockMvc.perform(delete("/api/tickets/1"))
                                .andExpect(status().isNoContent());
        }
}
