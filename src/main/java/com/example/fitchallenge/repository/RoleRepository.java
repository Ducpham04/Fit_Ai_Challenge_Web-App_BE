package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Object> findById(int i);
}
