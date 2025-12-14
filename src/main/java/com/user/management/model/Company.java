package com.user.management.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;


@Data
@Entity
@Table(name = "companies")
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address; // US-based recommended fields private String city;
    private String state; // e.g. CA, NY, TX
     private String contact_person;
     private String zipCode;
     private String email;
     private String phone;
     private String einNumber; // US Tax ID (optional but useful)
     @CreationTimestamp @Column(nullable = false, updatable = false)
     private Instant createdAt;
     @Enumerated(EnumType.STRING) @Column(nullable = false)
     private Status status;
     @PrePersist public void prePersist()
     { if (this.status == null)
     { this.status = Status.ACTIVE; }
     }
}

