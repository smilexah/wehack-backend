package com.ecommerce.wehackbackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizer_requests")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class OrganizerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Club club;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime reviewedAt;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}
