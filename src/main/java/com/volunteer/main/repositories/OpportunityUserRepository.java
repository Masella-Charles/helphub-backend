package com.volunteer.main.repositories;

import com.volunteer.main.entity.DonationDistributionEntity;
import com.volunteer.main.entity.OpportunityUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityUserRepository extends CrudRepository<OpportunityUserEntity, Integer> {
    Optional<OpportunityUserEntity> findById(Long id);

    List<OpportunityUserEntity> findByStatus(Boolean status);

    List<OpportunityUserEntity> findByUserId(Long userId);

    List<OpportunityUserEntity> findByOpportunityId(Long opportunityId);
    List<OpportunityUserEntity> findAll();

    Optional<OpportunityUserEntity> findByUserIdAndOpportunityId(Long userId,Long opportunityId);
}
