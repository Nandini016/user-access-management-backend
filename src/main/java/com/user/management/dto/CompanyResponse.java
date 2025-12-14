package com.user.management.dto;

import com.user.management.model.Status;
import java.time.Instant;
import lombok.Data;

@Data
public class CompanyResponse {
    private Long id;
    private String name;
    private String address;
    private String state;
    private String zipCode;
    private String contact_person;
    private String email;
    private String phone;
    private String einNumber;
    private Instant createdAt;
    private Status status;
}
