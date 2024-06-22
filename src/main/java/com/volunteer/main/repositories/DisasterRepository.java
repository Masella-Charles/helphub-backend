package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisasterRepository extends CrudRepository<DisasterEntity, Long> {

}
