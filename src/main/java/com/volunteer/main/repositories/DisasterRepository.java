package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisasterRepository extends CrudRepository<DisasterEntity, Long> {
    List<DisasterEntity> findByStatus(Boolean status);

    List<DisasterEntity> findByIdOrStatus(@Param("id") Long id, @Param("status") Boolean status);



}



