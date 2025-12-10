package com.user.management.controller;

import com.user.management.config.JwtUtil;
import com.user.management.dto.LoginRequest;
import com.user.management.dto.LoginResponse;
import com.user.management.dto.RegisterRequest;
import com.user.management.dto.UserResponse;
import com.user.management.model.Company;
import com.user.management.model.User;
import com.user.management.repository.CompanyRepository;
import com.user.management.repository.UserRepository;
import com.user.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CompanyRepository companyRepo;
    private final UserRepository repo;
    private final UserService service;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserRepository repo,CompanyRepository companyRepo, UserService service, JwtUtil jwtUtil) {
        this.companyRepo = companyRepo;
        this.repo = repo;
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        User user = repo.findByEmail(req.getEmail()).orElseThrow();

        if (!user.isApproved()) {
            throw new RuntimeException("User is not approved by admin yet");
        }
        if (encoder.matches(req.getPassword(), user.getPassword())) {
            String roleName = user.getRole().getName().name();
            return new LoginResponse(jwtUtil.generateToken(user), roleName);
        }
        throw new RuntimeException("Invalid credentials");
    }

    @GetMapping("/companies")
    public List<Company> listCompanies() {
        return companyRepo.findAllCompaniesOrderByCreatedAtDesc();
    }

    @GetMapping
    public List<UserResponse> listAll() {
        return service.listUsers();
    }
}
