package com.example.taskManager.web.model;

import com.example.taskManager.domain.TaskPriority;
import com.example.taskManager.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TaskDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    private TaskStatus status;

    @NotNull(message = "Priority cannot be null")
    private TaskPriority priority;
//
//    @NotNull(message = "Creator ID cannot be null")
//    private UUID creatorId;

    private UUID assigneeId;
    @Builder.Default
    private List<CommentDto> comments = new ArrayList<>();

}
