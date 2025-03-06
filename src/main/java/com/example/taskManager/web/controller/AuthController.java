package com.example.taskManager.web.controller;

import com.example.taskManager.services.AuthService;
import com.example.taskManager.web.model.JwtRequest;
import com.example.taskManager.web.model.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "User registration", description = "Registers a new user and returns a boolean status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/signUp")
    public ResponseEntity<Boolean> register(@RequestBody @Valid JwtRequest jwtRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(jwtRequest));
    }
    @Operation(summary = "User authentication", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody @Valid JwtRequest jwtRequest){
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authorization(jwtRequest));
    }
}
