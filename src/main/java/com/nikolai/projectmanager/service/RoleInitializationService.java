package com.nikolai.projectmanager.service;

import com.nikolai.projectmanager.model.Role;
import com.nikolai.projectmanager.model.RoleType;
import com.nikolai.projectmanager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleInitializationService implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initializeRoles();
    }

    private void initializeRoles() {
        log.info("Initializing roles...");

        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByRoleType(roleType).isEmpty()) {
                Role role = new Role(roleType);
                roleRepository.save(role);
                log.info("Created role: {} - {}", roleType.getRoleName(), roleType.getDescription());
            }
        }

        log.info("Role initialization completed.");
    }
}