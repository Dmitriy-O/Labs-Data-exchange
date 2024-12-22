package org.byovsiannikov.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies") // Додаємо @Table для відповідності data.sql
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва фільму не може бути порожньою")
    @Size(min = 1, max = 100, message = "Назва фільму повинна містити від 1 до 100 символів")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "Жанр фільму не може бути порожнім")
    @Size(min = 1, max = 50, message = "Жанр фільму повинен містити від 1 до 50 символів")
    @Column(nullable = false, length = 50)
    private String genre;

    @NotNull(message = "Тривалість фільму є обов'язковою")
    @Column(nullable = false)
    private Integer duration;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Screening> screenings;
}
