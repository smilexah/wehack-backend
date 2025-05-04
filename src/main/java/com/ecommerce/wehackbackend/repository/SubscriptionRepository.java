package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.Subscription;
import com.ecommerce.wehackbackend.model.entity.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    boolean existsById_UserIdAndId_ClubId(Long userId, Long clubId);

    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByClubId(Long clubId);

}
