package com.user.management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;


    private String password;


    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;


    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    // NEW: whether the admin has approved this user or not

    @Column(nullable = false)
    private boolean approved ;
    @CreationTimestamp
    @Column(nullable=false, updatable=false)
    private Instant createdAt;


}
