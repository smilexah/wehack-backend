package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.EventAttendance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendanceRepository extends CrudRepository<EventAttendance, Long> {
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
