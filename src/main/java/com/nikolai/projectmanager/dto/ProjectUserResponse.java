package com.nikolai.projectmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserResponse {

    private Long id;
    private UserResponse user;
    private ProjectResponse project;
    private String status;
    private String role;
}