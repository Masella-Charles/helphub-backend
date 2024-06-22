package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="permission")
@Data
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQ")
    @SequenceGenerator(name = "PERMISSION_SEQ", sequenceName = "PERMISSION_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long tId;

    @Column(name = "PERMISSION_NAME", unique = true, nullable = false)
    private String permissionName;

    @Column(name = "PERMISSION_DESCRIPTION", nullable = false)
    private String permissionDescription;

    @ManyToMany(mappedBy = "permissionEntities")
    private Set<RoleEntity> roles = new HashSet<>();
}