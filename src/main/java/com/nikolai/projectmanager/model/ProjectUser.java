package com.nikolai.projectmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_user",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "project_id"})})
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String status;

    @OneToMany(mappedBy = "projectUser")
    private Set<ProjectUserRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo")
    private Set<Task> tasksAssigned = new HashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Task> taskCreated = new HashSet<>();
}
