package org.byovsiannikov.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "film")
public class Film {
    @Id
    private String id;
    private String title;
    private String genre;
    private String releaseDate;
    private int duration;
    private Float price; // Додане поле ціни
}