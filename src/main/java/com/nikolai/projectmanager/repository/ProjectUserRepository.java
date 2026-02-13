package com.nikolai.projectmanager.repository;

import com.nikolai.projectmanager.model.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    Optional<ProjectUser> findByUserIdAndProjectId(Long userId, Long projectId);
    List<ProjectUser> findByUserId(Long userId);
    List<ProjectUser> findByProjectId(Long projectId);
}