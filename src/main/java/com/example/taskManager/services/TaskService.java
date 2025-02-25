package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import com.example.taskManager.domain.Comment;
import com.example.taskManager.domain.Task;
import com.example.taskManager.domain.TaskStatus;
import com.example.taskManager.web.model.TaskDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface TaskService {
   TaskEntity createTask(Task task);
   List<Task> findTask();
   Task findTask(UUID id);
   Task updateTask(UUID taskId, Task task);
   void deleteTask(UUID taskId);
   Task changeStatus(UUID taskId, TaskStatus status);
   Task assignTask(UUID taskId, UUID assigneeId);
   void addComment(UUID taskId, Comment comment);
}
