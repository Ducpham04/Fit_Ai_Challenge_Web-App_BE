package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Object> findById(int i);
}
