package com.example.taskManager.web.controller;

import com.example.taskManager.datasource.entity.task.TaskEntity;
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

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskDto taskDTO) {
        if (userHasAdminRole()) {
            Task createdTask = TaskEntityMapper.toDomain(taskService.createTask(TaskDtoMapper.toTaskDomain(taskDTO)));
            return ResponseEntity.ok(createdTask);
        }
        return ResponseEntity.status(403).body(null);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getTask() {
        List<Task> task = taskService.findTask();
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody @Valid TaskDto taskDTO) {
        if (userHasAdminRole() || isUserAssignedToTask(id)) {
            Task updatedTask = taskService.updateTask(id, TaskDtoMapper.toTaskDomain(taskDTO));
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null); // Forbidden if not admin or not assigned user
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        if (userHasAdminRole()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).body(null);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> changeTaskStatus(@PathVariable UUID id, @RequestParam TaskStatus status) {
        if (userHasAdminRole() || isUserAssignedToTask(id)) {
            Task updatedTask = taskService.changeStatus(id, status);
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null);
    }

    @PatchMapping("/{id}/assignee")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable UUID id, @RequestParam UUID userId) {
        if (userHasAdminRole()) {
            Task updatedTask = taskService.assignTask(id, userId);
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.status(403).body(null);
    }


//    @GetMapping
//    public ResponseEntity<Page<TaskDto>> getAllTasks(
//            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        return ResponseEntity.ok(taskService.getAllTasks(pageable));
//    }

//    @DeleteMapping("/{taskId}")
//    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
//        taskService.deleteTask(taskId);
//        return ResponseEntity.noContent().build();
//    }
//

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<HttpStatus> addComment(@PathVariable UUID taskId, @RequestBody @Valid CommentDto commentDto) {
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
