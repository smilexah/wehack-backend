package com.ecommerce.wehackbackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "event_attendance")
@IdClass(EventAttendanceId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendance {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "attended_at", nullable = false)
    private LocalDateTime attendedAt = LocalDateTime.now();

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;
}

@Embeddable
class EventAttendanceId implements Serializable {
    private Long userId;
    private Long eventId;
}