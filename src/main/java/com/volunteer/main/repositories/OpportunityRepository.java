package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.OpportunityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityRepository extends CrudRepository<OpportunityEntity, Long> {
    long countByRequiredVolunteers(OpportunityEntity opportunity);

    List<OpportunityEntity> findByStatus(Boolean status);

//    List<OpportunityEntity> findByStatus(@Param("status") Boolean status);

    List<OpportunityEntity> findAll();

    List<OpportunityEntity> findByDisasterEntityId(Long disasterId);


//    List<OpportunityEntity> findByDisasterId(Long disasterId);

    List<OpportunityEntity> findByIdAndStatus(Long opportunityId, Boolean status);

//    List<OpportunityEntity> findVolunteeringOpportunitiesByIdAndVolunteeringOpportunitiesByDisasterId(Long opportunityId, Long disasterId);

//    List<OpportunityEntity> findByStatusAndDisasterId(Boolean status, Long disasterId);


    @Query("SELECT o FROM OpportunityEntity o " +
            "WHERE (:id IS NULL OR o.id = :id) " +
            "AND (:status IS NULL OR o.status = :status) ")
    List<OpportunityEntity> findByCriteria(Long id, Boolean status);
}


