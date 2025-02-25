package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.datasource.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserEntity(user.getId(), user.getUsername(), user.getPassword(), user.getRoles()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User '%s' not found", email)));
    }
}
