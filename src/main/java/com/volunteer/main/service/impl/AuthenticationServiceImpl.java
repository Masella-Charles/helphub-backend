package com.volunteer.main.service.impl;

import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.LoginUserDto;
import com.volunteer.main.model.request.RegisterUserDto;
import com.volunteer.main.repositories.RoleRepository;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

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

        // Fetching the default role
        RoleEntity role = roleRepository.findByRoleName(input.getRole()).orElseThrow(() -> new RuntimeException("Role not found"));
        userEntity.setRole(role);

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

    @Override
    public UserEntity editUser(Long userId, RegisterUserDto editUserDto) {
        try {
            UserEntity user = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            user.setEmail(editUserDto.getEmail());
            user.setFullName(editUserDto.getFullName());

            // Update password if provided
            if (editUserDto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
            }

            // Update role if provided
            if (editUserDto.getRole() != null) {
                RoleEntity role = roleRepository.findByRoleName(editUserDto.getRole())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                user.setRole(role);
            }

            userRepository.save(user);

            return user; // Return the updated user entity directly
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(Math.toIntExact(userId));
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId, e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user", e);
        }
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email, String newPassword) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

            // Update user's password
            user.setPassword(passwordEncoder.encode(newPassword));

            userRepository.save(user);

            // Return success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password updated successfully for user with email: " + email);

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            // Handle case where user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with email: " + email);

        } catch (Exception e) {
            // Handle generic exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update password: " + e.getMessage());
        }
    }

    @Override
    public void editPassword(String email, String newPassword) {
        try {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));


            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Return a success message or status if needed

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password updated successfully" );
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
