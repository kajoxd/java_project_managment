package com.nikolai.projectmanager.repository;

import com.nikolai.projectmanager.model.Project;
import com.nikolai.projectmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByNameAndOwner(String name, User owner);
    List<Project> findByOwner(User owner);
    List<Project> findByNameContainingIgnoreCase(String name);
}