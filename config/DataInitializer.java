
package com.acxiom.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acxiom.crm.entity.Role;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.RoleName;
import com.acxiom.crm.repository.RoleRepository;
import com.acxiom.crm.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Value("${app.admin.email:admin@acxiomcrm.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@12345}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initializeData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Create roles if they do not already exist
            for (RoleName roleName : RoleName.values()) {

                if (roleRepository.findByName(roleName).isEmpty()) {

                    Role role = new Role();
                    role.setName(roleName);

                    roleRepository.save(role);
                }
            }

            // Create default administrator if not present
            if (!userRepository.existsByEmail(adminEmail)) {

                Role adminRole = roleRepository
                        .findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() ->
                            new IllegalStateException("Admin role not found"));

                User admin = new User();

                admin.setFullName("System Administrator");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(adminRole);
                admin.setActive(true);

                userRepository.save(admin);

                System.out.println("Default administrator created.");
            }
        };
    }
}
