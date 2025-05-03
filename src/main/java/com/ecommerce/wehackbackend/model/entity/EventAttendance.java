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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendance {

    @EmbeddedId
    private EventAttendanceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "attended_at", nullable = false)
    private LocalDateTime attendedAt = LocalDateTime.now();

    @Column(name = "points_awarded", nullable = false)
    private Integer pointsAwarded = 0;
}
