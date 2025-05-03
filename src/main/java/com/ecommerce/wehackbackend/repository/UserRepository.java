package com.ecommerce.wehackbackend.repository;

import com.ecommerce.wehackbackend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByTgLinkToken(String token);

    @Query(value = "SELECT u.* FROM users u WHERE u.is_active=true AND u.email=:email", nativeQuery = true )
    Optional<User> findByEmailAndIsActive(String email);

    @Query(value = "SELECT u.* FROM users u WHERE u.is_active=true AND u.tg_link_token=:token", nativeQuery = true )
    Optional<User> findByTgLinkTokenAndIsActive(String token);
}
