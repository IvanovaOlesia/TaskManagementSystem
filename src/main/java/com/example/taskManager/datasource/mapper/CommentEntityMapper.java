package com.example.taskManager.datasource.mapper;

import com.example.taskManager.datasource.entity.task.CommentEntity;
import com.example.taskManager.domain.Comment;
import com.example.taskManager.domain.Task;
import com.example.taskManager.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class CommentEntityMapper {
    private static TaskService taskService;
    @Autowired
    public CommentEntityMapper(TaskService taskService){
        this.taskService = taskService;
    }
    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        return Comment.builder()
                .id(entity.getId())
                .task(entity.getTask().getId())
                .author(entity.getAuthor())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    public static CommentEntity toEntity(Comment domain) {
        if (domain == null) {
            return null;
        }

        return CommentEntity.builder()
                .id(domain.getId())
                .author(domain.getAuthor())
                .content(domain.getContent())
                .task(TaskEntityMapper.toEntity(findTask(domain.getTask())))
                .createdAt(domain.getCreatedAt())
                .build();
    }

    private static Task findTask(UUID task) {
       return taskService.findTask(task);

    }
}
