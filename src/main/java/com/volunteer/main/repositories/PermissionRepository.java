package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.PermissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionEntity, Integer> {

}
