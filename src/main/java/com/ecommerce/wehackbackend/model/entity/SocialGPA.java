package com.ecommerce.wehackbackend.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "social_gpa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialGPA {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}