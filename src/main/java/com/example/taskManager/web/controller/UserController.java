package com.example.taskManager.web.controller;

import com.example.taskManager.services.UserService;
import com.example.taskManager.web.mapper.UserDtoMapper;
import com.example.taskManager.web.model.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDto user){
        userService.createUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Update an existing user", description = "Updates user information in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserDto user) {
        userService.updateUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "Delete an existing user", description = "Deletes a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody @Valid UserDto user) {
        userService.deleteUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
