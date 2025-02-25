package com.example.taskManager.domain;

import com.example.taskManager.datasource.entity.task.CommentEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@Builder
public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID creatorId;
    private UUID assigneeId;
    private List<Comment> comments = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
