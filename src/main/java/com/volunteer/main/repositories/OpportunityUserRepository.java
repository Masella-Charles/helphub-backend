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

    List<OpportunityUserEntity> findByStatusAndUserId(Boolean status, Long userId);

    List<OpportunityUserEntity> findByStatusAndOpportunityId(Boolean status, Long opportunityId);

    List<OpportunityUserEntity> findByStatusAndUserIdAndOpportunityId(Boolean status, Long userId, Long opportunityId);

    List<OpportunityUserEntity> findAll();

    long countByOpportunityId(Long opportunityId);

    @Query("SELECT COUNT(oue) FROM OpportunityUserEntity oue WHERE oue.opportunity.id = :opportunityId AND oue.status = true")
    long countByOpportunityIdAndStatusTrue(@Param("opportunityId") Long opportunityId);

    List<OpportunityUserEntity> findByUserIdAndOpportunityId(Long userId,Long opportunityId);

    Optional<OpportunityUserEntity> findByOpportunityIdAndUserId(Long opportunityId,Long userId);
}
