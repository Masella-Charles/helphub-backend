package com.volunteer.main.repositories;

import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.TimeSheetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSheetRepository extends CrudRepository<TimeSheetEntity, Long> {
    List<TimeSheetEntity> findByUserId(Long userId);
    Optional<TimeSheetEntity> findByIdAndUserId(Long id, Long userId);
}
