package com.user.management.controller;

import com.user.management.dto.RegisterRequest;
import com.user.management.model.User;
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
    @Autowired private RoleRepository roleRepo;
    @Autowired private CompanyRepository companyRepo;

    @GetMapping
    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setEmail(req.email);
        user.setPassword(new BCryptPasswordEncoder().encode(req.password));
        user.setCompany(companyRepo.findById(req.companyId).orElseThrow());
        user.setRole(roleRepo.findById(req.roleId).orElseThrow());
        user.setApproved(true);
        return userRepo.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody RegisterRequest req) {
        User user = userRepo.findById(id).orElseThrow();
        user.setEmail(req.email);
        user.setPassword(new BCryptPasswordEncoder().encode(req.password));
        user.setCompany(companyRepo.findById(req.companyId).orElseThrow());
        user.setRole(roleRepo.findById(req.roleId).orElseThrow());
        return userRepo.save(user);
    }

    @GetMapping("/pending")
    public List<User> listPendingUsers() {
        return userRepo.findByApprovedFalse();
    }

    @PutMapping("/{id}/approve")
    public User approveUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setApproved(true);
        return userRepo.save(user);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
    }
}
