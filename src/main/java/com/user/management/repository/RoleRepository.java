package com.user.management.repository;

import com.user.management.model.Role;
import com.user.management.model.RoleType;
import com.user.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
