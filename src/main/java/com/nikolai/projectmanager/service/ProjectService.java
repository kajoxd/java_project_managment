package com.nikolai.projectmanager.service;

import com.nikolai.projectmanager.dto.*;
import com.nikolai.projectmanager.exception.*;
import com.nikolai.projectmanager.model.Project;
import com.nikolai.projectmanager.model.ProjectUser;
import com.nikolai.projectmanager.model.Role;
import com.nikolai.projectmanager.model.RoleType;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.repository.ProjectRepository;
import com.nikolai.projectmanager.repository.ProjectUserRepository;
import com.nikolai.projectmanager.repository.RoleRepository;
import com.nikolai.projectmanager.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

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


        Role ownerRole = roleRepository.findByRoleType(RoleType.OWNER)
                .orElseThrow(() -> new RuntimeException("OWNER role not found in database"));

        ProjectUser projectUser = ProjectUser.builder()
                .user(currentUser)
                .project(savedProject)
                .role(ownerRole)
                .status("ACTIVE")
                .build();

        ProjectUser savedProjectUser = projectUserRepository.save(projectUser);

        return ProjectResponse.builder()
                .id(savedProject.getId())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .status(savedProject.getStatus())
                .ownerUsername(currentUser.getUsername())
                .ownerEmail(currentUser.getEmail())
                .build();
    }

    public ProjectUserResponse addUserToProject(Long projectId, @Valid AddUserToProjectRequest request, User currentUser) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        boolean hasPermission = isUserAuthorizedToAddUsers(currentUser.getId(), projectId);
        if (!hasPermission) {
            throw new InsufficientPermissionsException("You don't have permission to add users to this project. Only owners, admins, and managers can add users.");
        }

        User userToAdd = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        Role role = roleRepository.findById(request.getRoleId())
            .orElseThrow(() -> new RoleNotFoundException("Role with ID " + request.getRoleId() + " not found"));

        Optional<ProjectUser> existingProjectUser = projectUserRepository
            .findByUserIdAndProjectId(request.getUserId(), projectId);
        if (existingProjectUser.isPresent()) {
            throw new UserAlreadyInProjectException("User is already a member of this project");
        }

        ProjectUser projectUser = ProjectUser.builder()
            .user(userToAdd)
            .project(project)
            .role(role)
            .status("ACTIVE")
            .build();

        ProjectUser savedProjectUser = projectUserRepository.save(projectUser);

        return convertToProjectUserResponse(savedProjectUser);
    }

    private boolean isUserAuthorizedToAddUsers(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null && project.getOwner().getId().equals(userId)) {
            return true;
        }

        Optional<ProjectUser> projectUser = projectUserRepository.findByUserIdAndProjectId(userId, projectId);
        if (projectUser.isPresent()) {
            RoleType roleType = projectUser.get().getRole().getRoleType();
            return roleType == RoleType.OWNER || roleType == RoleType.ADMIN || roleType == RoleType.MANAGER;
        }

        return false;
    }

    private ProjectUserResponse convertToProjectUserResponse(ProjectUser projectUser) {
        UserResponse userResponse = new UserResponse(
                projectUser.getUser().getId(),
                projectUser.getUser().getEmail(),
                projectUser.getUser().getUsername(),
                projectUser.getUser().getFirstName(),
                projectUser.getUser().getLastName()
        );

        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectUser.getProject().getId())
                .name(projectUser.getProject().getName())
                .description(projectUser.getProject().getDescription())
                .status(projectUser.getProject().getStatus())
                .ownerUsername(projectUser.getProject().getOwner().getUsername())
                .build();

        return ProjectUserResponse.builder()
                .id(projectUser.getId())
                .user(userResponse)
                .project(projectResponse)
                .status(projectUser.getStatus())
                .role(projectUser.getRole().getName())
                .build();
    }
}