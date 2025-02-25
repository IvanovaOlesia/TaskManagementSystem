package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.domain.Role;
import com.example.taskManager.domain.User;
import com.example.taskManager.web.model.JwtRequest;
import com.example.taskManager.web.model.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    public boolean register(JwtRequest jwtRequest){
        if(userService.existsByUsername(jwtRequest.email())){
            return  false;
        }
        String hashPassword = passwordEncoder.encode(jwtRequest.password());
        userService.createUser(new User(jwtRequest.email(), hashPassword, Role.USER));
        return true;
    }
    public JwtResponse authorization(JwtRequest jwtRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.email(),jwtRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity clientEntity =(UserEntity)userDetailsService.loadUserByUsername(authentication.getName());
        return new JwtResponse(jwtProvider.generateAccessToken(clientEntity));
    }
}
