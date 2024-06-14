package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="PERMISSION")
@Data
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQ")
    @SequenceGenerator(name = "PERMISSION_SEQ", sequenceName = "PERMISSION_SEQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;

    private String name;

    @ManyToMany(mappedBy = "permissionEntities")
    private Set<RoleEntity> roles;
}
