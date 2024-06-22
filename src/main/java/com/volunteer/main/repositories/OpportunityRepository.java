package com.volunteer.main.repositories;

import com.volunteer.main.entity.DonationEntity;
import com.volunteer.main.entity.OpportunityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends CrudRepository<OpportunityEntity, Long> {
}
