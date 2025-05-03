package com.ecommerce.wehackbackend.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_loyalty_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoyaltyPoints {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}