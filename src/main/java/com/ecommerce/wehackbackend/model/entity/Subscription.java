package com.ecommerce.wehackbackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "club_subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Удалите @IdClass(SubscriptionId.class)
public class Subscription {
    @EmbeddedId
    private SubscriptionId id;

    @ManyToOne
    @MapsId("userId") // Ссылается на поле "userId" внутри SubscriptionId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("clubId") // Ссылается на поле "clubId" внутри SubscriptionId
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt = LocalDateTime.now();
}