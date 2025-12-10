package com.user.management.repository;

import com.user.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByApprovedFalse();
    @Query("SELECT u FROM User u WHERE u.approved = false ORDER BY u.createdAt DESC")
    List<User> findPendingUsersOrderByCreatedAtDesc();
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findAllUsersOrderByCreatedAtDesc();
}
