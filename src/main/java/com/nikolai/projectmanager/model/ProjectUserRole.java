package com.nikolai.projectmanager.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_user_role",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"project_user_id", "role_id"})})
public class ProjectUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_user_id", nullable = false)
    private ProjectUser projectUser;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
