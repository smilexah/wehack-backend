package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.UserLoyaltyPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoyaltyPointsRepository extends JpaRepository<UserLoyaltyPoints, Long> {
    Optional<UserLoyaltyPoints> findByUserId(Long userId);
}
