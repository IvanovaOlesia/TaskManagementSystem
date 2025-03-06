package com.example.taskManager.web.controller;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.datasource.mapper.TaskEntityMapper;
import com.example.taskManager.datasource.repository.UserRepository;
import com.example.taskManager.domain.Role;
import com.example.taskManager.domain.Task;
import com.example.taskManager.domain.TaskStatus;
import com.example.taskManager.domain.exception.UserNotFoundException;
import com.example.taskManager.services.TaskService;
import com.example.taskManager.web.mapper.CommentDtoMapper;
import com.example.taskManager.web.mapper.TaskDtoMapper;
import com.example.taskManager.web.model.CommentDto;
import com.example.taskManager.web.model.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;
    @Operation(summary = "Создание задачи", description = "Только администратор может создать задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskDto taskDTO) {
        if (userHasAdminRole()) {
            Task createdTask = TaskEntityMapper.toDomain(taskService.createTask(TaskDtoMapper.toTaskDomain(taskDTO)));
            return ResponseEntity.ok(createdTask);
        }
        return ResponseEntity.status(403).body(null);
    }
    @Operation(summary = "Get all tasks", description = "Retrieve all tasks.")
    @ApiResponse(responseCode = "200", description = "List of tasks retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getTask() {
        List<Task> task = taskService.findTask();
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Update a task", description = "Admins or assigned users can update a task.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not authorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody @Valid TaskDto taskDTO) {
        if (userHasAdminRole() || isUserAssignedToTask(id)) {
            Task updatedTask = taskService.updateTask(id, TaskDtoMapper.toTaskDomain(taskDTO));
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null); // Forbidden if not admin or not assigned user
    }
    @Operation(summary = "Delete a task", description = "Admins can delete tasks.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can delete tasks")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        if (userHasAdminRole()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).body(null);
    }
    @Operation(summary = "Change task status", description = "Admins or assigned users can change task status.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> changeTaskStatus(@PathVariable UUID id, @RequestParam TaskStatus status) {
        if (userHasAdminRole() || isUserAssignedToTask(id)) {
            Task updatedTask = taskService.changeStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null);
    }
    @Operation(summary = "Assign a task to a user", description = "Only admins can assign tasks to users.")
    @PatchMapping("/{id}/assignee")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable UUID id, @RequestParam UUID userId) {
        if (userHasAdminRole()) {
            Task updatedTask = taskService.assignTask(id, userId);
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null);
    }

    @Operation(summary = "Add a comment to a task", description = "Users can add comments to tasks.")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<HttpStatus> addComment(@PathVariable UUID taskId, @RequestBody @Valid CommentDto commentDto) {
        commentDto.setTask(taskId);
        commentDto.setAuthor(((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        taskService.addComment(taskId, CommentDtoMapper.toDomain(commentDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    private boolean userHasAdminRole() {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()-> new UserNotFoundException("User not found"));
        return user.getAuthorities().contains(Role.ADMIN);
    }

    private boolean isUserAssignedToTask(UUID taskId) {
        UserEntity user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()-> new UserNotFoundException("User not found"));
        Task task = taskService.findTask(taskId);
        return task.getAssigneeId().equals(user.getId());
    }
}
