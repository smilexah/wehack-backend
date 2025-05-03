package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.EventReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventReviewRepository extends JpaRepository<EventReview, Long> {
    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    @Query("SELECT AVG(r.rating) FROM EventReview r WHERE r.event.id = :eventId")
    Double calculateAverageRatingByEventId(Long eventId);
}
