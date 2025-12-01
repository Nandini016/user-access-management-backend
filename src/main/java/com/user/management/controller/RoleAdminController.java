package com.user.management.controller;

import com.user.management.model.Role;
import com.user.management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
public class RoleAdminController {

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping
    public List<Role> listRoles() {
        return roleRepo.findAll();
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepo.save(role);
    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role updated) {
        Role role = roleRepo.findById(id).orElseThrow();
        role.setName(updated.getName());
        return roleRepo.save(role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleRepo.deleteById(id);
    }
}
