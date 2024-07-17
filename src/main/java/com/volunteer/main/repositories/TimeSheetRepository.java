package com.volunteer.main.repositories;

import com.volunteer.main.entity.OpportunityUserEntity;
import com.volunteer.main.entity.TimeSheetEntity;
import com.volunteer.main.model.response.TimeSheetResponseDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSheetRepository extends CrudRepository<TimeSheetEntity, Long> {
    List<TimeSheetEntity> findByUserId(Long userId);
    List<TimeSheetEntity> findByIdAndUserId(Long id, Long userId);
    @Query(value = "SELECT t.id, u.id as user_id, o.id as opportunity_id, o.name as opportunity_name, t.start_time, t.end_time, t.status, t.created_at, t.updated_at " +
            "FROM timesheet_table t " +
            "JOIN users u ON t.user_id = u.T_ID " +
            "JOIN volunteering_opportunities o ON t.opportunity_id = o.T_ID " +
            "WHERE u.T_ID = :userId", nativeQuery = true)
    List<Object[]> findTimeSheetsByUserId(Long userId);

}
