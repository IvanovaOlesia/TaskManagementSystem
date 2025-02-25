package com.example.taskManager.web.controller;

import com.example.taskManager.services.AuthService;
import com.example.taskManager.web.model.JwtRequest;
import com.example.taskManager.web.model.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;
    @PostMapping("/signUp")
    public ResponseEntity<Boolean> register(@RequestBody JwtRequest jwtRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(jwtRequest));
    }
    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authorization(jwtRequest));
    }
}
