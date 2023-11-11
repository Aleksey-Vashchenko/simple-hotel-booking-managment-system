package com.vashchenko.project.models.entities;

import com.vashchenko.project.enums.BookingStatus;
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
@EqualsAndHashCode(exclude = "guests")
@ToString(exclude = "guests")
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "date_on", nullable = false)
    private LocalDate dateOn;
    @Column(name = "date_of", nullable = false)
    private LocalDate dateOf;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status",nullable = false)
    private BookingStatus status;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "many_bookings_to_many_guests",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    @Builder.Default
    private List<Guest> guests = new ArrayList<>();
    @Transient
    private short amountOfGuests;
    public void addGuest(Guest guest){
        guests.add(guest);
    }
    public void initAmountOfGuests(){
        if(!this.guests.isEmpty()){
            this.amountOfGuests=(short) guests.size();
        }
    }
}
