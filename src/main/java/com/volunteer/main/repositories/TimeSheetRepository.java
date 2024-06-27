package com.volunteer.main.repositories;

import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.TimeSheetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSheetRepository extends CrudRepository<TimeSheetEntity, Long> {
    List<TimeSheetEntity> findByUserId(Long userId);
}
