package com.example.taskManager.web.mapper;

import com.example.taskManager.domain.User;
import com.example.taskManager.web.model.UserDto;

public class UserDtoMapper {
    private UserDtoMapper() {}
    public static User toDomain(UserDto userDto) {
        return User.builder()
//                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
