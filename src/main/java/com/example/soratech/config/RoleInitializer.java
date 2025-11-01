package com.example.soratech.config;

import com.example.soratech.model.Role;
import com.example.soratech.model.User;
import com.example.soratech.repository.RoleRepository;
import com.example.soratech.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            // Создание ролей только если они не существуют
            if (roleRepository.findByName("Администратор").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("Администратор");
                adminRole.setDescription("Полный доступ ко всем функциям системы");
                adminRole.setDeleted(false);
                roleRepository.save(adminRole);
                System.out.println("✓ Роль 'Администратор' создана");
            }

            if (roleRepository.findByName("Менеджер").isEmpty()) {
                Role managerRole = new Role();
                managerRole.setName("Менеджер");
                managerRole.setDescription("Управление товарами, заказами и отзывами");
                managerRole.setDeleted(false);
                roleRepository.save(managerRole);
                System.out.println("✓ Роль 'Менеджер' создана");
            }

            if (roleRepository.findByName("Клиент").isEmpty()) {
                Role clientRole = new Role();
                clientRole.setName("Клиент");
                clientRole.setDescription("Доступ к личному кабинету и оформлению заказов");
                clientRole.setDeleted(false);
                roleRepository.save(clientRole);
                System.out.println("✓ Роль 'Клиент' создана");
            }

            System.out.println("\n=== Роли успешно инициализированы ===\n");
            
            // Назначение роли "Клиент" всем пользователям без роли
            Role clientRole = roleRepository.findByName("Клиент").orElse(null);
            if (clientRole != null) {
                List<User> users = userRepository.findAll();
                int updatedCount = 0;
                for (User user : users) {
                    if (user.getRole() == null) {
                        user.setRole(clientRole);
                        userRepository.save(user);
                        updatedCount++;
                    }
                }
                if (updatedCount > 0) {
                    System.out.println("✓ Назначена роль 'Клиент' для " + updatedCount + " пользователей без роли");
                }
            }
        };
    }
}



