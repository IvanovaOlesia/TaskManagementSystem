package com.example.taskManager.web.model;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class CommentDto {
    private UUID id;
    private UUID task;
    private UUID author;
    private String content;
    private LocalDateTime createdAt;
}
