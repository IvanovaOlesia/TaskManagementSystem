package com.example.taskManager.web.model;

import com.example.taskManager.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Builder
@Data
public class UserDto {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;
    @NotNull(message = "Roles cannot be null")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
