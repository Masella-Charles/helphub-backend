package com.volunteer.main.model.request;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;

    private String password;
}
