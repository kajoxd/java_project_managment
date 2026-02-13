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
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "role")
    private Set<ProjectUserRole> projectUserRoles = new HashSet<>();

    public Role(RoleType roleType) {
        this.roleType = roleType;
        this.name = roleType.getRoleName();
        this.description = roleType.getDescription();
    }
}