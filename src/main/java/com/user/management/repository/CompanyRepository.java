package com.user.management.repository;

import com.user.management.model.Company;
import com.user.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c ORDER BY c.createdAt DESC")
    List<Company> findAllCompaniesOrderByCreatedAtDesc();

}
