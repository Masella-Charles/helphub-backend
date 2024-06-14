package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionRepository, Integer> {

}
