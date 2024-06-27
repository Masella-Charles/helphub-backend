package com.volunteer.main.repositories;


import com.volunteer.main.entity.TestimonialEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialRepository extends CrudRepository<TestimonialEntity, Long> {
    List<TestimonialEntity> findByStatus(Boolean status);
    List<TestimonialEntity> findByUserId(Long userId);
}
