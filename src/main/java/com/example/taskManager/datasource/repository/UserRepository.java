package com.example.taskManager.datasource.repository;

import com.example.taskManager.datasource.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(UUID id);
    boolean existsByEmail(String email);

//    @Query("SELECT COUNT(*) FROM users u WHERE 'ADMIN' = ANY(role)")
    @Query("SELECT COUNT(u) FROM UserEntity u JOIN u.roles r WHERE r = 'ADMIN'")
    long countAdmins();
}
