package com.nikolai.projectmanager.repository;

import com.nikolai.projectmanager.model.Role;
import com.nikolai.projectmanager.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByRoleType(RoleType roleType);
}