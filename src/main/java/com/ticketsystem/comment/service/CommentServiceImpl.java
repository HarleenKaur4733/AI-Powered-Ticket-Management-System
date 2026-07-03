package com.ticketsystem.comment.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    // private final TicketRepository ticketRepository;
    // private final UserRepository userRepository;
    private final TicketService ticketService;
    private final UserService userService;

    @Override
    @Transactional
    public CommentResponse addComment(Long ticketId,
            CreateCommentRequest request) {

        Ticket ticket = ticketService.getTicketById(ticketId);

        String email = SecurityUtils.getCurrentUsername();

        User user = userService.getUserByEmail(email);

        Comment comment = commentMapper.toEntity(request);

        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);
    }

    @Override
    public Page<CommentResponse> getComments(Long ticketId,
            Pageable pageable) {

        return commentRepository
                .findByTicketId(ticketId, pageable)
                .map(commentMapper::toResponse);
    }
}