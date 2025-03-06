package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.datasource.mapper.CommentEntityMapper;
import com.example.taskManager.datasource.mapper.TaskEntityMapper;
import com.example.taskManager.datasource.repository.CommentRepository;
import com.example.taskManager.datasource.repository.TaskRepository;
import com.example.taskManager.datasource.repository.UserRepository;
import com.example.taskManager.domain.Comment;
import com.example.taskManager.domain.Role;
import com.example.taskManager.domain.Task;
import com.example.taskManager.domain.TaskStatus;
import com.example.taskManager.domain.exception.ForbiddenFieldException;
import com.example.taskManager.domain.exception.TaskNotFoundException;
import com.example.taskManager.domain.exception.UserNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public TaskEntity createTask(Task task) {
        task.setCreatorId(((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        return taskRepository.save(TaskEntityMapper.toEntity(task));

    }

    @Override
    @Transactional
    public List<Task> findTask() {
        return taskRepository.findAll().stream().map(TaskEntityMapper::toDomain).toList();
    }

    @Override
    public  Task findTask(UUID id) {
        return taskRepository.findById(id).map(TaskEntityMapper::toDomain).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }


    @Override
    public Task updateTask(UUID taskId, Task task) {
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!currentUser.getAuthorities().contains(Role.ADMIN)) {
            validateUserFields(taskEntity, task);
        }
        if (currentUser.getAuthorities().contains(Role.ADMIN)) {
            taskEntity.setTitle(task.getTitle());
            taskEntity.setDescription(task.getDescription());
            taskEntity.setPriority(task.getPriority());
            taskEntity.setStatus(task.getStatus());
            taskEntity.setAssignee(userRepository.findById(task.getAssigneeId()).orElseThrow(() -> new UserNotFoundException("User not found")));
        } else {
            if (task.getStatus() != null && !task.getStatus().equals(taskEntity.getStatus())) {
                taskEntity.setStatus(task.getStatus());
            }

            if (task.getComments() != null && !task.getComments().isEmpty()) {
                for (Comment commentDTO : task.getComments()) {
                    Comment comment = new Comment();
                    comment.setContent(commentDTO.getContent());
                    comment.setTask(task.getId());
                    comment.setAuthor(currentUser.getId());
                    commentRepository.save(CommentEntityMapper.toEntity(comment));
                }
            }
        }

        taskRepository.save(taskEntity);
        return TaskEntityMapper.toDomain(taskEntity);
    }

    @Override
    public void deleteTask(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task changeStatus(UUID taskId, TaskStatus status) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskEntity.setStatus(status);
        return TaskEntityMapper.toDomain(taskRepository.save(taskEntity));
    }

    @Override
    public Task assignTask(UUID taskId, UUID assigneeId) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskEntity.setAssignee(userRepository.findById(assigneeId).orElseThrow(() -> new UserNotFoundException("User not found")));
        taskRepository.save(taskEntity);
        return TaskEntityMapper.toDomain(taskRepository.save(taskEntity));
    }

    @Override
    @Transactional
    public void addComment(UUID taskId, Comment comment) {
        Task taskEntity =TaskEntityMapper.toDomain(taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found")));
        comment.setTask(taskEntity.getId());
        comment.setAuthor(((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        commentRepository.save(CommentEntityMapper.toEntity(comment));
    }


    private void validateUserFields(TaskEntity taskEntity, Task task) {
        if (task.getTitle() != null && !task.getTitle().equals(taskEntity.getTitle())) {
            throw new ForbiddenFieldException("You are not allowed to change the title of the task.");
        }
        if (task.getDescription() != null && !task.getDescription().equals(taskEntity.getDescription())) {
            throw new ForbiddenFieldException("You are not allowed to change the description of the task.");
        }
        if (task.getPriority() != null && !task.getPriority().equals(taskEntity.getPriority())) {
            throw new ForbiddenFieldException("You are not allowed to change the priority of the task.");
        }
        if (task.getAssigneeId() != null && !task.getAssigneeId().equals(taskEntity.getAssignee())) {
            throw new ForbiddenFieldException("You are not allowed to change the assignee of the task.");
        }
    }
}
