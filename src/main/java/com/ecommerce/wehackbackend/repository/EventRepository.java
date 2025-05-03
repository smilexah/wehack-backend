package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.Event;
import com.ecommerce.wehackbackend.model.entity.EventReview;
import com.ecommerce.wehackbackend.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByVenueId(Long venueId);

    @Query("SELECT t FROM Ticket t WHERE t.event.id = :eventId")
    List<Ticket> findTicketsByEventId(Long eventId);

    @Query("SELECT r FROM EventReview r WHERE r.event.id = :eventId")
    List<EventReview> findReviewsByEventId(Long eventId);
}
