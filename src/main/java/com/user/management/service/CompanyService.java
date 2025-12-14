package com.user.management.service;

import com.user.management.dto.CompanyResponse;
import com.user.management.model.Company;
import com.user.management.model.Status;
import com.user.management.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepo;

    @Transactional
    public void softDeleteCompany(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setStatus(Status.INACTIVE);
        companyRepo.save(company);
    }

    @Transactional
    public void hardDeleteCompany(Long id) {
        if (!companyRepo.existsById(id)) {
            throw new RuntimeException("Company not found");
        }
        companyRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CompanyResponse getCompany(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        CompanyResponse res = new CompanyResponse();
        res.setId(company.getId());
        res.setName(company.getName());
        res.setAddress(company.getAddress());
        res.setState(company.getState());
        res.setZipCode(company.getZipCode());
        res.setContact_person(company.getContact_person());
        res.setEmail(company.getEmail());
        res.setPhone(company.getPhone());
        res.setEinNumber(company.getEinNumber());
        res.setCreatedAt(company.getCreatedAt());
        res.setStatus(company.getStatus());

        return res;
    }

}

