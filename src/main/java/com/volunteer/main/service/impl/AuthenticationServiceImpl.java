package com.volunteer.main.service.impl;

import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.LoginUserDto;
import com.volunteer.main.model.request.RegisterUserDto;
import com.volunteer.main.model.request.RoleDTO;
import com.volunteer.main.repositories.RoleRepository;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.AuthenticationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Log log = LogFactory.getLog(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserEntity signup(RegisterUserDto input) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(input.getEmail());
        userEntity.setFullName(input.getFullName());
        userEntity.setPassword(passwordEncoder.encode(input.getPassword()));

//        // Fetching the default role
//        RoleEntity role = roleRepository.findByRoleName(input.getRole()).orElseThrow(() -> new RuntimeException("Role not found"));
//        userEntity.setRole(role);

        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
