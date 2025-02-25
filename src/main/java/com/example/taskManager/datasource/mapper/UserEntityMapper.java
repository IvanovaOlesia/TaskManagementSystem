package com.example.taskManager.datasource.mapper;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.domain.User;
import org.springframework.stereotype.Service;


public class UserEntityMapper {
    private UserEntityMapper(){}
    public static UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .password(user.getPassword())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .password(entity.getPassword())
                .build();

    }
}
