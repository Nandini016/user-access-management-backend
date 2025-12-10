package com.user.management.controller;

import com.user.management.dto.RegisterRequest;
import com.user.management.model.RoleType;
import com.user.management.model.User;
import com.user.management.model.Role;
import com.user.management.repository.CompanyRepository;
import com.user.management.repository.RoleRepository;
import com.user.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private CompanyRepository companyRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public List<User> listUsers() {
        return userRepo.findAllUsersOrderByCreatedAtDesc();
    }

    @PostMapping
    public User createUser(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCompany(companyRepo.findById(req.getCompanyId()).orElseThrow());

        // Assign role: since RegisterRequest no longer contains roleId,
        // pick a sensible default role by name (try "USER" then "ROLE_USER"), otherwise fallback to any role.
        Role role = roleRepo.findByName(RoleType.USER)
                        .orElseGet(() -> roleRepo.findAll().stream().findFirst()
                                .orElseThrow(() -> new IllegalStateException("No roles configured in the system")));

        user.setRole(role);
        user.setApproved(true);
        return userRepo.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody RegisterRequest req) {
        User user = userRepo.findById(id).orElseThrow();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCompany(companyRepo.findById(req.getCompanyId()).orElseThrow());

        // same role logic as create: keep existing role if you want, or reassign default if your flow requires it.
        Role role = roleRepo.findByName(RoleType.USER)
                        .orElseGet(() -> roleRepo.findAll().stream().findFirst()
                                .orElseThrow(() -> new IllegalStateException("No roles configured in the system")));

        user.setRole(role);
        return userRepo.save(user);
    }

    @GetMapping("/pending")
    public List<User> listPendingUsers() {
        return userRepo.findPendingUsersOrderByCreatedAtDesc();
    }

    @PutMapping("/{id}/approve")
    public User approveUser(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean approve) {
        User user = userRepo.findById(id).orElseThrow();
        user.setApproved(approve);
        return userRepo.save(user);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
    }
}
