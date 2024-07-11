package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.VolunteerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends CrudRepository<VolunteerEntity, Long> {
    Optional<VolunteerEntity> findByUserId(Long userId);

}
