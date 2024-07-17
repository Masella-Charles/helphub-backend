package com.volunteer.main.controller;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.LoginUserDto;
import com.volunteer.main.model.request.RegisterUserDto;
import com.volunteer.main.model.response.LoginResponseDTO;
import com.volunteer.main.service.AuthenticationService;
import com.volunteer.main.utils.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }


    @PutMapping("/editUser")
    public ResponseEntity<UserEntity> editUser(@RequestBody RegisterUserDto editUserDto) {
        Long userId = editUserDto.getId();
        UserEntity updatedUser = authenticationService.editUser(userId, editUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User Endpoint
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestBody RegisterUserDto editUserDto) {
        Long userId = editUserDto.getId();
        authenticationService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Forgot Password Endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody RegisterUserDto editUserDto) {
        String email = editUserDto.getEmail();
        String newPassword = editUserDto.getPassword();
        return authenticationService.forgotPassword(email, newPassword);
    }

    // Edit Password Endpoint
    @PutMapping("/edit-password")
    public ResponseEntity<Void> editPassword(@RequestBody RegisterUserDto editUserDto) {
        String email = editUserDto.getEmail();
        String newPassword = editUserDto.getPassword();
        authenticationService.editPassword(email, newPassword);
        return ResponseEntity.noContent().build();
    }
}