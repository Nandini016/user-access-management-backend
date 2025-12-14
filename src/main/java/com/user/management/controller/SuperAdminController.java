package com.user.management.controller;

import com.user.management.dto.CompanyResponse;
import com.user.management.model.Company;
import com.user.management.repository.CompanyRepository;
import com.user.management.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/companies")
public class SuperAdminController {

    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    CompanyService companyService;

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        Company saved = companyRepo.save(company);
        return saved;
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company updated) {
        Company company = companyRepo.findById(id).orElseThrow();
        company.setName(updated.getName());
        return companyRepo.save(company);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.softDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDeleteCompany(@PathVariable Long id) {
        companyService.hardDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
