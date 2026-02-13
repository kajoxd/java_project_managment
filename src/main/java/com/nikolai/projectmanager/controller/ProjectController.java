package com.nikolai.projectmanager.controller;

import com.nikolai.projectmanager.dto.CreateProjectRequest;
import com.nikolai.projectmanager.dto.ProjectResponse;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        ProjectResponse response = projectService.createProject(request, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}