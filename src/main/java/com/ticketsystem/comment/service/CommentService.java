package com.ticketsystem.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ticketsystem.comment.dto.CommentResponse;
import com.ticketsystem.comment.dto.CreateCommentRequest;

public interface CommentService {

    CommentResponse addComment(Long ticketId,
            Long userId,
            CreateCommentRequest request);

    Page<CommentResponse> getComments(Long ticketId,
            Pageable pageable);

}