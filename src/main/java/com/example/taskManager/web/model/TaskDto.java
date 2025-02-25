package com.example.taskManager.web.model;

import com.example.taskManager.domain.TaskPriority;
import com.example.taskManager.domain.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TaskDto {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID creatorId;
    private UUID assigneeId;
    private List<CommentDto> comments = new ArrayList<>();

}
