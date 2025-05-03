package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.OrganizerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRequestRepository extends JpaRepository<OrganizerRequest, Long> {
    boolean existsByUserIdAndClubId(Long userId, Long clubId);
}
