package com.example.taskManager.web.model;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class CommentDto {
//    private UUID id;
//    @NotNull(message = "Task ID cannot be null")
    private UUID task;
//    @NotNull(message = "Author ID cannot be null")
    private UUID author;
    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 500, message = "Comment content must not exceed 500 characters")
    private String content;
    private LocalDateTime createdAt;
}
