package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.DonationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends CrudRepository<DonationEntity, Long> {
    List<DonationEntity> findByStatus(Boolean status);

    List<DonationEntity> findByDisasterEntityId(Long disasterId);

}
