package com.volunteer.main.service.impl;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.LoginUserDto;
import com.volunteer.main.model.request.RegisterUserDto;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserEntity signup(RegisterUserDto input) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(input.getEmail());
        userEntity.setFullName(input.getFullName());
        userEntity.setPassword(passwordEncoder.encode(input.getPassword()));

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
