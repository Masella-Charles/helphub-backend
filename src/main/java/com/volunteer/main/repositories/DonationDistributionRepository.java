package com.volunteer.main.repositories;

import com.volunteer.main.entity.DonationDistributionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationDistributionRepository extends CrudRepository<DonationDistributionEntity, Long> {
    List<DonationDistributionEntity> findByDonationId(Long donationId);
}
