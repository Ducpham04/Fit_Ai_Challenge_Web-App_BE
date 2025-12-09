package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.Entity.Role;
import com.example.fitchallenge.repository.RoleRepository;
import com.example.fitchallenge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<Role> getRoles() {
        return (roleRepository.findAll());
    }
}
