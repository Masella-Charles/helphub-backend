package com.volunteer.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Entity
@Table(name="VOLUNTEER_ROLES")
@Getter
@Setter
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEER_ROLE_SEQ")
    @SequenceGenerator(name = "VOLUNTEER_ROLE_SEQ", sequenceName = "VOLUNTEER_ROLE_SEQ", allocationSize = 1)
    @Column(name = "T_ID")
    private Long tId;

    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private String roleName;

    @Column(name = "ROLE_DESCRIPTION", unique = true, nullable = false)
    private String roleDescription;

    @OneToMany(mappedBy = "role")
    @JsonBackReference
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissionEntities = new HashSet<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (PermissionEntity permission : permissionEntities) {
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return permission.getPermissionName();
                }
            });
        }
        return authorities;
    }
}