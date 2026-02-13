package com.nikolai.projectmanager.service;

import com.nikolai.projectmanager.dto.CreateProjectRequest;
import com.nikolai.projectmanager.dto.ProjectResponse;
import com.nikolai.projectmanager.model.Project;
import com.nikolai.projectmanager.model.ProjectUser;
import com.nikolai.projectmanager.model.ProjectUserRole;
import com.nikolai.projectmanager.model.Role;
import com.nikolai.projectmanager.model.RoleType;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.repository.ProjectRepository;
import com.nikolai.projectmanager.repository.ProjectUserRepository;
import com.nikolai.projectmanager.repository.ProjectUserRoleRepository;
import com.nikolai.projectmanager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final RoleRepository roleRepository;
    private final ProjectUserRoleRepository projectUserRoleRepository;

    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, User currentUser) {
        if (projectRepository.findByNameAndOwner(request.getName(), currentUser).isPresent()) {
            throw new RuntimeException("You already have a project with name '" + request.getName() + "'");
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .owner(currentUser)
                .build();

        Project savedProject = projectRepository.save(project);

        ProjectUser projectUser = ProjectUser.builder()
                .user(currentUser)
                .project(savedProject)
                .status("ACTIVE")
                .build();

        ProjectUser savedProjectUser = projectUserRepository.save(projectUser);

        Role ownerRole = roleRepository.findByRoleType(RoleType.OWNER)
                .orElseThrow(() -> new RuntimeException("OWNER role not found in database"));

        ProjectUserRole projectUserRole = ProjectUserRole.builder()
                .projectUser(savedProjectUser)
                .role(ownerRole)
                .build();

        projectUserRoleRepository.save(projectUserRole);

        return ProjectResponse.builder()
                .id(savedProject.getId())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .status(savedProject.getStatus())
                .ownerUsername(currentUser.getUsername())
                .ownerEmail(currentUser.getEmail())
                .build();
    }
}