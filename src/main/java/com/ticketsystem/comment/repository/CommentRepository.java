package com.ticketsystem.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketsystem.comment.entity.Comment;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment> findByTicketId(Long ticketId);
}