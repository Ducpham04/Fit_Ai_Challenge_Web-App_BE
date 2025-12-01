package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.Entity.Role;
import com.example.FIT_Challenge.repository.RoleRepository;
import com.example.FIT_Challenge.service.RoleService;
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
