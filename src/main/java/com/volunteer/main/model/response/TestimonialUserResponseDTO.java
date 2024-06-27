package com.volunteer.main.model.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.volunteer.main.entity.RoleEntity;
import com.volunteer.main.entity.VolunteerEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TestimonialUserResponseDTO {
    private ResponseStatus responseStatus;
    private Long id;
    private String testimonial;
    private Boolean status;
    private LocalDate createdAt;
    private UserEntity userEntity;

    @Data
    public static class UserEntity{
        private Long tId;
        private String fullName;
        private String email;
        private String password;
        private Date createdAt;
        private Date updatedAt;
        private RoleEntity role;
        private VolunteerEntity volunteer;

    }
}
