package com.user.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;        // original filename
    private String storageName;     // unique stored filename (UUID)
    private String contentType;
    private Long size;
    private Long companyId;         // FK to company
    private Long uploadedByUserId;  // who uploaded
    private Instant uploadedAt;
    private String description;     // optional
    private String path;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }


}
