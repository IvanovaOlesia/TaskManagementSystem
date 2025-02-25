package com.example.taskManager.web.mapper;

import com.example.taskManager.datasource.mapper.TaskEntityMapper;
import com.example.taskManager.domain.Comment;
import com.example.taskManager.web.model.CommentDto;

public class CommentDtoMapper {
    private CommentDtoMapper() {}
    public static Comment toDomain(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .task(commentDto.getTask())
                .createdAt(commentDto.getCreatedAt())
                .content(commentDto.getContent())
                .author(commentDto.getAuthor())
                .build();

    }
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .task(comment.getTask())
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .build();
    }
}
