package com.volunteer.main.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="VOLUNTEERING_OPPORTUNITIES")
@Data
public class OpportunityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOL_OPP_SEQ")
    @SequenceGenerator(name = "VOL_OPP_SEQ", sequenceName = "VOL_OPP_SEQ", allocationSize = 1)
    @Column(name= "T_ID")
    private Long id;

    private String name;
    private String description;
    private LocalDate date;
    private Integer requiredVolunteers;
    private Double hours;
    private Boolean status;


    @OneToMany(mappedBy = "opportunityEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OpportunityImageEntity> opportunityImages;

    @ManyToOne(optional = true)
    @JoinColumn(name = "disaster_id", referencedColumnName = "T_ID")
    private DisasterEntity disasterEntity;


    @Override
    public String toString() {
        return "OpportunityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", requiredVolunteers=" + requiredVolunteers +
                ", hours=" + hours +
                ", status=" + status +
//                ", opportunityImages=" + opportunityImages +
                ", disasterEntity=" + (disasterEntity != null ? disasterEntity.getId() : null) +
                '}';
    }
}
