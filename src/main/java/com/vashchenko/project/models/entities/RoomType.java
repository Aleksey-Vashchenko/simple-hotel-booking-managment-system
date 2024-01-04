package com.vashchenko.project.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor()
@EqualsAndHashCode(exclude = "images")
@ToString(exclude = "images")
@SuperBuilder
@Table(name = "type")
public class RoomType{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String name;
    @Column(name = "amount_of_people")
    private int amountOfAdult;
    @Column(name = "room_amount")
    private int roomAmount;
    private double price;
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Image> images = new HashSet<>();



}
