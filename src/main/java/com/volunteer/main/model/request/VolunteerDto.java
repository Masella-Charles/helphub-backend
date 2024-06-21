package com.volunteer.main.model.request;

import lombok.Data;

@Data
public class VolunteerDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String additionalInfo;
}
