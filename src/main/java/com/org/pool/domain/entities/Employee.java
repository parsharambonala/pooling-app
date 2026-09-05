package com.org.pool.domain.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id
    @Column(name = "employee_id", nullable = false, updatable = false)
    private Integer employeeId;

    @Column(name = "mail_id", nullable = false, updatable = false, unique = true)
    private String mailId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "manager")
    private String manager;

    @Column(name = "division")
    private String division;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @Builder.Default
    @Column(name = "is_active")
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Ride> rides = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
