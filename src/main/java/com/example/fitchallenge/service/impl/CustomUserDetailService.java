package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 🔹 Tìm người dùng theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        // 🔹 Trả về đối tượng UserDetails mà Spring Security hiểu được
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())               // username = email
                .password(user.getPassword())                 // mật khẩu đã mã hoá (BCrypt)
                .authorities(user.getRole().getRoleName())    // quyền lấy từ Role (nếu có)
                .build();
    }
}
