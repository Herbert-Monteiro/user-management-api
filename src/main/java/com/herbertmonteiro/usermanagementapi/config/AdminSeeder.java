package com.herbertmonteiro.usermanagementapi.config;

import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.enums.Role;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("ADMIN_EMAIL/ADMIN_PASSWORD não definidos: admin de demo não foi criado");
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin de demo já existe, nada a fazer");
            return;
        }

        User admin = User.builder()
                .name("Admin Demo")
                .cpf("00000000000")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        userRepository.save(admin);
        log.info("Admin de demo criado: {}", adminEmail);
    }
}
