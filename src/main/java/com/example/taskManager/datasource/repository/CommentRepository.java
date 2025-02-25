package com.example.taskManager.datasource.repository;

import com.example.taskManager.datasource.entity.task.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
}
