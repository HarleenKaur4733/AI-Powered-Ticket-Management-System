package com.ticketsystem.ticket.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ticketsystem.exception.ResourceNotFoundException;
import com.ticketsystem.ticket.dto.CreateTicketRequest;
import com.ticketsystem.ticket.dto.TicketResponse;
import com.ticketsystem.ticket.dto.UpdateTicketRequest;
import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.entity.TicketPriority;
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

        @InjectMocks
        private TicketServiceImpl ticketService;

        @Test
        void shouldCreateTicketSuccessfully() {

                CreateTicketRequest request = CreateTicketRequest.builder()
                                .title("Login Issue")
                                .description("Cannot login")
                                .assignedToUserId(1L)
                                .build();

                User assignedUser = User.builder()
                                .id(1L)
                                .name("Harleen")
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
                                .assignedTo(assignedUser)
                                .build();

                TicketResponse response = TicketResponse.builder()
                                .id(10L)
                                .title("Login Issue")
                                .status(TicketStatus.OPEN)
                                .build();

                when(ticketMapper.toEntity(request)).thenReturn(ticket);
                when(userService.getUserById(1L)).thenReturn(assignedUser);
                when(ticketRepository.save(ticket)).thenReturn(savedTicket);
                when(ticketMapper.toResponse(savedTicket)).thenReturn(response);

                TicketResponse result = ticketService.createTicket(request);

                assertNotNull(result);
                assertEquals(10L, result.getId());
                assertEquals(TicketStatus.OPEN, result.getStatus());

                ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
                verify(ticketRepository).save(captor.capture());

                Ticket captured = captor.getValue();
                assertEquals(TicketStatus.OPEN, captured.getStatus());
                assertEquals(assignedUser, captured.getAssignedTo());
                assertNotNull(captured.getCreatedAt());
        }

        @Test
        void shouldGetTicketSuccessfully() {

                Ticket ticket = Ticket.builder()
                                .id(1L)
                                .title("Login Issue")
                                .status(TicketStatus.OPEN)
                                .build();

                TicketResponse response = TicketResponse.builder()
                                .id(1L)
                                .title("Login Issue")
                                .status(TicketStatus.OPEN)
                                .build();

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
                when(ticketMapper.toResponse(ticket)).thenReturn(response);

                TicketResponse result = ticketService.getTicket(1L);

                assertNotNull(result);
                assertEquals(1L, result.getId());

                verify(ticketRepository).findById(1L);
                verify(ticketMapper).toResponse(ticket);
        }

        @Test
        void shouldThrowExceptionWhenTicketNotFound() {

                when(ticketRepository.findById(100L)).thenReturn(Optional.empty());

                ResourceNotFoundException ex = assertThrows(
                                ResourceNotFoundException.class,
                                () -> ticketService.getTicket(100L));

                assertEquals("Ticket not found", ex.getMessage());

                verify(ticketRepository).findById(100L);
                verify(ticketMapper, never()).toResponse(any());
        }

        @Test
        void shouldReturnAllTickets() {

                Pageable pageable = PageRequest.of(0, 10);

                Ticket ticket = Ticket.builder().id(1L).title("Bug").build();

                TicketResponse response = TicketResponse.builder()
                                .id(1L)
                                .title("Bug")
                                .build();

                Page<Ticket> page = new PageImpl<>(List.of(ticket));

                when(ticketRepository.findAll(pageable)).thenReturn(page);
                when(ticketMapper.toResponse(ticket)).thenReturn(response);

                Page<TicketResponse> result = ticketService.getAllTickets(pageable);

                assertEquals(1, result.getTotalElements());
                verify(ticketRepository).findAll(pageable);
        }

        @Test
        void shouldUpdateTicketSuccessfully() {

                UpdateTicketRequest request = UpdateTicketRequest.builder()
                                .status(TicketStatus.IN_PROGRESS)
                                .priority(TicketPriority.HIGH)
                                .assignedToUserId(1L)
                                .build();

                User user = User.builder().id(1L).build();

                Ticket ticket = Ticket.builder()
                                .id(1L)
                                .status(TicketStatus.OPEN)
                                .build();

                TicketResponse response = TicketResponse.builder()
                                .id(1L)
                                .status(TicketStatus.IN_PROGRESS)
                                .build();

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
                when(userService.getUserById(1L)).thenReturn(user);
                when(ticketRepository.save(ticket)).thenReturn(ticket);
                when(ticketMapper.toResponse(ticket)).thenReturn(response);

                TicketResponse result = ticketService.updateTicket(1L, request);

                assertEquals(TicketStatus.IN_PROGRESS, result.getStatus());

                verify(ticketRepository).save(ticket);
        }

        @Test
        void shouldThrowExceptionWhenUpdatingMissingTicket() {

                UpdateTicketRequest request = UpdateTicketRequest.builder().build();

                when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> ticketService.updateTicket(1L, request));

                verify(ticketRepository, never()).save(any());
        }

        @Test
        void shouldDeleteTicketSuccessfully() {

                Ticket ticket = Ticket.builder().id(1L).build();

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

                ticketService.deleteTicket(1L);

                verify(ticketRepository).findById(1L);
                verify(ticketRepository).deleteById(1L);
        }

        @Test
        void shouldThrowExceptionWhenDeletingMissingTicket() {

                when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> ticketService.deleteTicket(1L));

                verify(ticketRepository, never()).deleteById(anyLong());
        }

        @Test
        void shouldReturnTicketById() {

                Ticket ticket = Ticket.builder().id(1L).build();

                when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

                Ticket result = ticketService.getTicketById(1L);

                assertEquals(ticket, result);
        }

        @Test
        void shouldThrowExceptionWhenGetTicketByIdFails() {

                when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class,
                                () -> ticketService.getTicketById(1L));
        }
}
