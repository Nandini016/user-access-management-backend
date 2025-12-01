package com.user.management.dto;

public class RegisterRequest {
    public String email;
    public String password;
    public Long companyId;
    public Long roleId;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getRoleId() {
        return roleId;
    }
}
