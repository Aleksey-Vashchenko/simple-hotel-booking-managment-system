package com.vashchenko.project.models.entities;

import com.vashchenko.project.enums.BookingStatus;
import com.vashchenko.project.repositories.RoomTypeRepository;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "date_on", nullable = false)
    private LocalDate dateOn;
    @Column(name = "date_of", nullable = false)
    private LocalDate dateOf;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status",nullable = false)
    private BookingStatus status;
    @ManyToOne()
    @JoinColumn(name = "type_id", nullable = true)
    private RoomType roomType;
    @Column(name = "guest_amount")
    private Integer guestAmount;
    @Column(name = "past_type")
    private String pastType;
}
