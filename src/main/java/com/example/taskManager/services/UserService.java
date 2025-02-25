package com.example.taskManager.services;

import com.example.taskManager.datasource.entity.user.UserEntity;
import com.example.taskManager.datasource.mapper.UserEntityMapper;
import com.example.taskManager.datasource.repository.UserRepository;
import com.example.taskManager.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public boolean existsByUsername(String username){
        return userRepository.existsByEmail(username);
    }
    public UserEntity createUser(User user){
        return userRepository.save(UserEntityMapper.toEntity(user));
    }
    public long countAdmins(){
        return userRepository.countAdmins();
    }
    public void deleteUser(User user){
        userRepository.delete(UserEntityMapper.toEntity(user));
    }
    public void updateUser(User user){
        userRepository.save(UserEntityMapper.toEntity(user));
    }
    public User getUser(User user){
        return userRepository.findById(user.getId()).map(UserEntityMapper::toDomain).orElse(null);
    }
}
