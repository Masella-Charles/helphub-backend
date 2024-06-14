package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="VOLUNTEER_ROLES")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEER_ROLE_SEQ")
    @SequenceGenerator(name = "VOLUNTEER_ROLE_SEQ", sequenceName = "VOLUNTEER_ROLE_SEQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long tId;
    @Column(name= "ROLE_NAME",unique = true, nullable = false)
    private String roleName;
    //@ManyToMany(mappedBy = "roles")
    //private Set<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissionEntities;

}
