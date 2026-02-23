package com.nikolai.projectmanager.controller;

import com.nikolai.projectmanager.dto.AddUserToProjectRequest;
import com.nikolai.projectmanager.dto.CreateProjectRequest;
import com.nikolai.projectmanager.dto.ProjectResponse;
import com.nikolai.projectmanager.dto.ProjectUserResponse;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        ProjectResponse response = projectService.createProject(request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{projectId}/add")
    public ResponseEntity<ProjectUserResponse> addUserToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody AddUserToProjectRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        ProjectUserResponse response = projectService.addUserToProject(projectId, request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}