package com.volunteer.main.controller;

import com.volunteer.main.entity.PermissionEntity;
import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.RoleDTO;
import com.volunteer.main.repositories.UserRepository;
import com.volunteer.main.service.UserService;
import com.volunteer.main.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @GetMapping("/profile")
    public ResponseEntity<UserEntity> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> allUsers() {
        List <UserEntity> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/by-role")
    public ResponseEntity<List<UserEntity>> getUsersByRoleName(@RequestBody RoleDTO roleNameRequest) {
        List<UserEntity> users = userService.getUsersByRoleName(roleNameRequest.getRoleName());
        return ResponseEntity.ok(users);
    }


}