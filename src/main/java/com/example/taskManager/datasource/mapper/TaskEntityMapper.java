package com.example.taskManager.datasource.mapper;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.datasource.repository.UserRepository;
import com.example.taskManager.domain.Task;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class TaskEntityMapper {
private static  UserRepository userRepository;

    @Autowired
    public TaskEntityMapper(UserRepository userRepository) {
        this.userRepository = userRepository;  // Инициализация через конструктор
    }
    public static Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .creatorId(entity.getCreator() != null ? entity.getCreator().getId() : null)
                .assigneeId(entity.getAssignee() != null ? entity.getAssignee().getId() : null)
                .comments(entity.getComments() != null ?
                        entity.getComments().stream().map(CommentEntityMapper::toDomain).collect(Collectors.toList()) :
                        new ArrayList<>())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static TaskEntity toEntity(Task domain) {
        if (domain == null) {
            return null;
        }

        UserEntity creator = domain.getCreatorId() != null ? findUserById(domain.getCreatorId()) : null;
        UserEntity assignee = domain.getAssigneeId() != null ? findUserById(domain.getAssigneeId()) : null;

        return TaskEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .status(domain.getStatus())
                .priority(domain.getPriority())
                .creator(creator)
                .assignee(assignee)
                .comments(domain.getComments() != null ?
                        domain.getComments().stream().map(CommentEntityMapper::toEntity).collect(Collectors.toList()) :
                        new ArrayList<>())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
    private static UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
