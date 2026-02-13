package com.nikolai.projectmanager.model;

public enum RoleType {
    OWNER("Project owner with full access and control"),
    ADMIN("Project administrator with management privileges"),
    DEVELOPER("Developer with code access and task management"),
    VIEWER("Read-only access to project information"),
    TESTER("Testing and quality assurance role"),
    MANAGER("Project manager with planning and coordination access");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRoleName() {
        return this.name();
    }
}