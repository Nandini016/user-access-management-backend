package com.user.management.repository;

import com.user.management.model.Company;
import com.user.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
