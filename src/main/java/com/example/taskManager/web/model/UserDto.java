package com.example.taskManager.web.model;

import com.example.taskManager.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Builder
@Data
public class UserDto {
    private UUID id;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
}
