package com.nikolai.projectmanager.repository;

import com.nikolai.projectmanager.model.ProjectUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRoleRepository extends JpaRepository<ProjectUserRole, Long> {
    Optional<ProjectUserRole> findByProjectUserIdAndRoleId(Long projectUserId, Long roleId);
    List<ProjectUserRole> findByProjectUserId(Long projectUserId);
    List<ProjectUserRole> findByRoleId(Long roleId);
}