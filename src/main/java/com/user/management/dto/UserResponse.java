package com.user.management.dto;

import com.user.management.model.Company;
import com.user.management.model.Role;

public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private Company company;

    public UserResponse(Long id, String email, Role role, Company company) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.company = company;
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
}
