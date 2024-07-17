package com.volunteer.main.repositories;

import com.volunteer.main.entity.OpportunityUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityUserRepository extends CrudRepository<OpportunityUserEntity, Integer> {
    Optional<OpportunityUserEntity> findById(Long id);

    List<OpportunityUserEntity> findAll();

    long countByOpportunityId(Long opportunityId);

}
