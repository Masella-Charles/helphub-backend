package com.volunteer.main.repositories;

import com.volunteer.main.entity.DisasterEntity;
import com.volunteer.main.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

}
