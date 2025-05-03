package com.ecommerce.wehackbackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Column(name = "streaming_url")
    private String streamingUrl;

    private Double price;
    private Integer capacity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventReview> reviews;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventAttendance> attendances;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<UserEventReminder> reminders;
}