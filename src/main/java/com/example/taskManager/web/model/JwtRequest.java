package com.example.taskManager.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JwtRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password) {
}
