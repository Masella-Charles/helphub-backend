package com.volunteer.main.repositories;

import com.volunteer.main.entity.OpportunityImageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityImageRepository extends CrudRepository<OpportunityImageEntity, Long> {
    Optional<OpportunityImageEntity> findByOpportunityEntity_Id(Long opportunityId);
}
