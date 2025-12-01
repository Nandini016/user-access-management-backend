package com.user.management.service;

import com.user.management.config.JwtUtil;
import com.user.management.dto.LoginRequest;
import com.user.management.dto.LoginResponse;
import com.user.management.dto.RegisterRequest;
import com.user.management.dto.UserResponse;
import com.user.management.model.User;
import com.user.management.repository.CompanyRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private CompanyRepository companyRepo;
    @Autowired private JwtUtil jwtUtil;

    public UserResponse register(RegisterRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));
        user.setCompany(companyRepo.findById(req.getCompanyId()).orElseThrow());
        user.setRole(roleRepo.findById(req.getRoleId()).orElseThrow());
        user.setApproved(false); // pending
        userRepo.save(user);

        return new UserResponse(user.getId(), user.getEmail(), user.getRole(), user.getCompany());
    }


    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.email).orElseThrow();

        // MUST check approval
        if (!user.isApproved()) {
            throw new RuntimeException("User is not approved by admin yet");
        }

        if (!new BCryptPasswordEncoder().matches(req.password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String roleName = user.getRole().getName().name();
        return new LoginResponse(jwtUtil.generateToken(user), roleName);
    }

    public List<UserResponse> listUsers() {
        return userRepo.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getEmail(), u.getRole(), u.getCompany()))
                .collect(Collectors.toList());
    }

    public List<UserResponse> listPendingUsers() {
        return userRepo.findByApprovedFalse().stream()
                .map(u -> new UserResponse(u.getId(), u.getEmail(), u.getRole(), u.getCompany()))
                .collect(Collectors.toList());
    }


    public UserResponse approveUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setApproved(true);
        userRepo.save(user);

        return new UserResponse(user.getId(), user.getEmail(), user.getRole(), user.getCompany());
    }
}
