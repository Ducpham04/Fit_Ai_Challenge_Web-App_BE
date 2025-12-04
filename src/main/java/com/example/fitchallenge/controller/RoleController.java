package com.example.fitchallenge.controller;

import com.example.fitchallenge.Entity.Role;
import com.example.fitchallenge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }
}
