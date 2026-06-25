package com.ticketsystem.comment.mapper;

import org.springframework.stereotype.Component;

import com.ticketsystem.comment.dto.CommentResponse;
import com.ticketsystem.comment.dto.CreateCommentRequest;
import com.ticketsystem.comment.entity.Comment;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequest request) {

        if (request == null) {
            return null;
        }

        return Comment.builder()
                .content(request.getContent())
                .build();
    }

    public CommentResponse toResponse(Comment comment) {

        if (comment == null) {
            return null;
        }

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getUser().getId())
                .authorName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}