package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.user.JwtResponse;
import com.example.FIT_Challenge.DTO.user.LoginRequest;
import com.example.FIT_Challenge.DTO.user.RegisterRequestAdmin;
import com.example.FIT_Challenge.DTO.user.UserDTO;
import com.example.FIT_Challenge.Entity.User;
import com.example.FIT_Challenge.Security.JWT.JwtTokenProvider;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.User.UserRepository;
import com.example.FIT_Challenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public  void register(RegisterRequestAdmin registerRequestAdmin){
        if(userRepository.existsByEmail(registerRequestAdmin.getEmail())){
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setEmail(registerRequestAdmin.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestAdmin.getPassword()));
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Optional<User> user  = userRepository.findByEmail((loginRequest.getEmail())) ;
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            throw new RuntimeException("Invalid password");
        }
        String token = jwtTokenProvider.generateToken(user.get().getEmail());
        System.out.println(token);
        return new JwtResponse(token);
    }


    @Override
    public UserDetails loadUserByEmail(String email)  {
        // Tìm user trong DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        // Trả về một đối tượng UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities((GrantedAuthority) user.getRole()) // có thể lấy từ role của user
                .build();
    }

    @Override
    public NotificationResponse getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
        .stream().map(user ->  {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getUserName());
            dto.setRole(user.getRole().getRoleName());

    return dto ;
        }).collect(Collectors.toList()); // ẩn password trước khi trả về

        return new NotificationResponse(true, "Success", users);


    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(user -> {;
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getUserName());
            dto.setRole(user.getRole().getRoleName());
            return dto;
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().getRoleName()
        );

    }

    private void toUser(){

    }
}
