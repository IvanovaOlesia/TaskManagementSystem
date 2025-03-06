package com.example.taskManager.web.mapper;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.domain.Task;
import com.example.taskManager.web.model.TaskDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class TaskDtoMapper {
    private TaskDtoMapper(){}
public static TaskDto toTaskDto(Task task){
    return TaskDto.builder()
            .title(task.getTitle())
            .description(task.getDescription())
            .priority(task.getPriority())
            .status(task.getStatus())
            .assigneeId(task.getAssigneeId())
//            .creatorId(task.getCreatorId())
            .build();
}
public static Task toTaskDomain(TaskDto taskDto) {
    return Task.builder()
            .title(taskDto.getTitle())
            .description(taskDto.getDescription())
            .priority(taskDto.getPriority())
            .status(taskDto.getStatus())
            .assigneeId(taskDto.getAssigneeId())
            .creatorId(((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
            .build();
}
}
