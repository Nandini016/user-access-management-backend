package com.user.management.dto;

import com.user.management.model.Company;
import com.user.management.model.Role;
import com.user.management.model.Status;

public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private Company company;
    private boolean approved;    // ⭐ ADD THIS FIELD
    private Status status;

    public UserResponse(Long id, String email, Role role, Company company,boolean approved,Status status) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.company = company;
        this.approved = approved;
        this.status = status;
    }
    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public Role getRole() {
        return role;
    }
    public Company getCompany() {
        return company;
    }
    public boolean isApproved() { return approved; }
    public Status getStatus() { return status; }
}
