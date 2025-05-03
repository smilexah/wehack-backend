package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByVenueId(Long venueId);
}
