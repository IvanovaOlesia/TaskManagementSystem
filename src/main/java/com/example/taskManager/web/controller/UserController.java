package com.example.taskManager.web.controller;

import com.example.taskManager.services.UserService;
import com.example.taskManager.web.mapper.UserDtoMapper;
import com.example.taskManager.web.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto user){
        userService.createUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDto user) {
        userService.updateUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody UserDto user) {
        userService.deleteUser(UserDtoMapper.toDomain(user));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
