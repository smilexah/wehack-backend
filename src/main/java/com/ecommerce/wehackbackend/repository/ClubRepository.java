package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.Club;
import com.ecommerce.wehackbackend.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("SELECT e FROM Event e WHERE e.club.id = :clubId")
    List<Event> findEventsByClubId(Long clubId);
}
