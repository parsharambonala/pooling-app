package com.org.pool.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", updatable = false, nullable = false)
    private Employee driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", updatable = false, nullable = false)
    private Vehicle vehicle;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RideStatusEnum status;

    @Column(name = "pickup_address", nullable = false)
    private String pickUpAddress;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "distance_from_destination")
    private Double distanceFromDestination;

    @Builder.Default
    @OneToMany(mappedBy = "ride")
    private List<Booking> bookings = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
