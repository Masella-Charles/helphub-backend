package com.volunteer.main.service;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.LoginUserDto;
import com.volunteer.main.model.request.RegisterUserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    UserEntity signup(RegisterUserDto input);
    UserEntity authenticate(LoginUserDto input);
}

