package com.vashchenko.project.models.entities;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"type"})
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne(cascade = {PERSIST, REFRESH, DETACH,MERGE,REFRESH})
    @JoinColumn(name = "type_id")
    private RoomType type;
    @Override
    public String toString(){
        return url;
    }
}
