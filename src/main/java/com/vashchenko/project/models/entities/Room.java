package com.vashchenko.project.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.CascadeType.*;

@Entity
@Data
@NoArgsConstructor()
@AllArgsConstructor
@EqualsAndHashCode(exclude = "bookings")
@ToString(exclude = "bookings")
@SuperBuilder
@Table(name = "room")
public class Room{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {PERSIST, REFRESH, DETACH,MERGE,REFRESH})
    @JoinColumn(name = "type_id")
    private RoomType type;
    private String number;
}
