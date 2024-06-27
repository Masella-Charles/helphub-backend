package com.volunteer.main.repositories;

import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends CrudRepository<OpportunityEntity, Long> {
    long countByRequiredVolunteers(OpportunityEntity opportunity);

    List<OpportunityEntity> findByStatus(Boolean status);

    List<OpportunityEntity> findByDisasterEntityId(Long disasterId);
}
