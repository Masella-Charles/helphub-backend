package com.volunteer.main.service;

import com.volunteer.main.entity.UserEntity;
import com.volunteer.main.model.request.RegisterUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    List<UserEntity> allUsers();

    List<UserEntity> getUsersByRoleName(String roleName);


}
