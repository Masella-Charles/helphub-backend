package com.volunteer.main.repositories;

import com.volunteer.main.entity.ContactUsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends CrudRepository<ContactUsEntity, Long> {
}
