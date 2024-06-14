package com.volunteer.main.repositories;

import com.volunteer.main.entity.DonationDistributionEntity;
import com.volunteer.main.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationDistributionRepository extends CrudRepository<DonationDistributionEntity, Integer> {

}
