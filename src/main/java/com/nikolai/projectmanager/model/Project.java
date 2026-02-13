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
@Table(name = "project",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "owner_id"})})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "project")
    private Set<ProjectUser> users = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();
}
