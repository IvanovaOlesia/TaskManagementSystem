package com.example.taskManager;


import com.example.taskManager.domain.Role;
import com.example.taskManager.domain.User;
import com.example.taskManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
            if (userService.countAdmins() == 0) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userService.createUser(admin);
                System.out.println("✅ Администратор создан: admin@example.com / admin123");
            }

    }
}
