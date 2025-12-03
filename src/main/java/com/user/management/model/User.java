package com.user.management.model;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String email;

    @Setter
    private String password;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    // NEW: whether the admin has approved this user or not
    @Setter
    @Column(nullable = false)
    private boolean approved ; // default: false for newly registered users

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Company getCompany() {
        return company;
    }

    public boolean isApproved() {
        return approved;
    }

}
