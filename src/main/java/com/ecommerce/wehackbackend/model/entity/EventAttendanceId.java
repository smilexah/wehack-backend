package com.ecommerce.wehackbackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendanceId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "event_id")
    private Long eventId;
}
