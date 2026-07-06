package com.ticketsystem.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ticketsystem.comment.dto.CommentResponse;
import com.ticketsystem.comment.dto.CreateCommentRequest;
import com.ticketsystem.comment.entity.Comment;
import com.ticketsystem.comment.mapper.CommentMapper;
import com.ticketsystem.comment.repository.CommentRepository;
import com.ticketsystem.security.SecurityUtils;
import com.ticketsystem.ticket.entity.Ticket;
import com.ticketsystem.ticket.service.TicketService;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void shouldAddCommentSuccessfully() {

        Long ticketId = 1L;

        CreateCommentRequest request = CreateCommentRequest.builder()
                .content("Looks good")
                .build();

        Ticket ticket = Ticket.builder().id(ticketId).build();

        User user = User.builder()
                .id(10L)
                .email("harleen@gmail.com")
                .build();

        Comment comment = Comment.builder()
                .content("Looks good")
                .build();

        Comment savedComment = Comment.builder()
                .id(100L)
                .content("Looks good")
                .ticket(ticket)
                .user(user)
                .build();

        CommentResponse response = CommentResponse.builder()
                .id(100L)
                .content("Looks good")
                .build();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticket);
        when(userService.getUserByEmail("harleen@gmail.com")).thenReturn(user);
        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(response);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUsername)
                    .thenReturn("harleen@gmail.com");

            CommentResponse result = commentService.addComment(ticketId, request);

            assertNotNull(result);
            assertEquals(100L, result.getId());

            ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
            verify(commentRepository).save(captor.capture());

            Comment captured = captor.getValue();

            assertEquals(ticket, captured.getTicket());
            assertEquals(user, captured.getUser());
            assertNotNull(captured.getCreatedAt());
        }
    }

    @Test
    void shouldReturnCommentsByTicketId() {

        Pageable pageable = PageRequest.of(0, 10);

        Comment comment = Comment.builder()
                .id(1L)
                .content("Test Comment")
                .build();

        CommentResponse response = CommentResponse.builder()
                .id(1L)
                .content("Test Comment")
                .build();

        Page<Comment> page = new PageImpl<>(List.of(comment));

        when(commentRepository.findByTicketId(1L, pageable))
                .thenReturn(page);

        when(commentMapper.toResponse(comment))
                .thenReturn(response);

        Page<CommentResponse> result = commentService.getComments(1L, pageable);

        assertEquals(1, result.getTotalElements());

        verify(commentRepository)
                .findByTicketId(1L, pageable);
    }
}
