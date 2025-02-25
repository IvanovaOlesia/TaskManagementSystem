package com.example.taskManager.domain;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private UUID id;
    private UUID task;
    private UUID author;
    private String content;
    private LocalDateTime createdAt;
}
