package com.example.taskManager.datasource.repository;

import com.example.taskManager.datasource.entity.task.TaskEntity;
import com.example.taskManager.domain.TaskStatus;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findAll();

    List<TaskEntity> findByStatus(String status);

    List<TaskEntity> findByPriority(String priority);

    Page<TaskEntity> findByStatus(String status, Pageable pageable);

    List<TaskEntity> findByTitleContaining(String title);
    @Modifying
    @Query("UPDATE TaskEntity t SET t.status = :status WHERE t.id = :taskId")
    void updateTaskStatus(@Param("taskId") UUID taskId, @Param("status") TaskStatus status);
    @Modifying
    @Query("DELETE FROM TaskEntity t WHERE t.status = :status")
    void deleteByStatus(@Param("status") TaskStatus status);


}
