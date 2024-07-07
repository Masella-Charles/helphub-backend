package com.volunteer.main.model.request;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ContactUsRequestDTO {

    private Long id;

    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
    private String stage;
}
