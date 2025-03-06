package com.example.taskManager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String email;
    private String password;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    public User(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.roles.add(role);
    }
    public void setRole(Role roles) {
        this.roles.add(roles);
    }
}
