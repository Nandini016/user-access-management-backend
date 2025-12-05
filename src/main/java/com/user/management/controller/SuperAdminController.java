package com.user.management.controller;

import com.user.management.model.Company;
import com.user.management.repository.CompanyRepository;
import com.user.management.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/companies")
public class SuperAdminController {

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private SchemaService schemaService;


    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        Company saved = companyRepo.save(company);
        try {
            schemaService.createCompanySchemaWithTransactionTable(company.getName());
        } catch (Exception e) {
            companyRepo.delete(saved);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create schema/table");
        }
        return saved;
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable Long id, @RequestBody Company updated) {
        Company company = companyRepo.findById(id).orElseThrow();
        company.setName(updated.getName());
        return companyRepo.save(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id) {
        companyRepo.deleteById(id);
    }
}
