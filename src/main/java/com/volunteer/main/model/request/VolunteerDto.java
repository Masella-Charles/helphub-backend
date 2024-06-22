package com.volunteer.main.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volunteer.main.entity.UserEntity;
import lombok.Data;

import java.math.BigInteger;

@Data
public class VolunteerDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String additionalInfo;
    private int userId;
}
