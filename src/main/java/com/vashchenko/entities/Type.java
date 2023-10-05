package com.vashchenko.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private String name;
    @Column(name = "amount_of_adult")
    private int amountOfAdult;
    @Column(name = "amount_of_children")
    private int amountOfChildren;
    @OneToMany(mappedBy = "type")
    private List<Room> rooms;

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", amountOfAdult=" + amountOfAdult +
                ", amountOfChildren=" + amountOfChildren +
                '}';
    }
}
