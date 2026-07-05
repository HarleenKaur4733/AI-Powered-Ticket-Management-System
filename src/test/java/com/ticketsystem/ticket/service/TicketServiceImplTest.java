package com.ticketsystem.ticket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketStatus;
import com.ticketsystem.ticket.mapper.TicketMapper;
import com.ticketsystem.ticket.repository.TicketRepository;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

        @Mock
        private TicketRepository ticketRepository;

        @Mock
        private TicketMapper ticketMapper;

        @Mock
        private UserService userService;

        @InjectMocks // class to test
        private TicketServiceImpl ticketService;

        // fake objects
        CreateTicketRequest request = CreateTicketRequest.builder()
                        .title("Login Issue")
                        .description("Cannot login")
                        .assignedToUserId(1L)
                        .build();

        User testUser = User.builder()
                        .id(1L)
                        .name("Harleen")
                        .email("harleen@gmail.com")
                        .build();

        Ticket ticket = Ticket.builder()
                        .title("Login Issue")
                        .description("Cannot login")
                        .build();

        Ticket savedTicket = Ticket.builder()
                        .id(10L)
                        .title("Login Issue")
                        .description("Cannot login")
                        .status(TicketStatus.OPEN)
                        .assignedTo(testUser)
                        .build();

        TicketResponse response = TicketResponse.builder()
                        .id(10L)
                        .title("Login Issue")
                        .status(TicketStatus.OPEN)
                        .build();

        // mock behavior
        @Test
        void shouldCreateTicketSuccessfully() {

                // Arrange
                when(ticketMapper.toEntity(request))
                                .thenReturn(ticket);

                when(userService.getUserById(1L))
                                .thenReturn(testUser);

                when(ticketRepository.save(ticket))
                                .thenReturn(savedTicket);

                when(ticketMapper.toResponse(savedTicket))
                                .thenReturn(response);

                // Act
                TicketResponse result = ticketService.createTicket(request);

                // Assert
                assertNotNull(result);

                assertEquals(10L, result.getId());

                assertEquals("Login Issue", result.getTitle());

                assertEquals(TicketStatus.OPEN,
                                result.getStatus());

                // Check whether the dependencies were actually called.
                verify(ticketMapper)
                                .toEntity(request);

                verify(userService)
                                .getUserById(1L);

                verify(ticketRepository)
                                .save(ticket);

                verify(ticketMapper)
                                .toResponse(savedTicket);
        }

}
