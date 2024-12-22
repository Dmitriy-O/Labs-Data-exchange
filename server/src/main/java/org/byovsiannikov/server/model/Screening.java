package org.byovsiannikov.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "screenings") // Додаємо @Table для відповідності data.sql
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull(message = "Start time is mandatory")
    @Column(name = "date_time", nullable = false)
    private LocalDateTime startTime; // Змінено на LocalDateTime

    @NotBlank(message = "Hall is mandatory")
    @Column(nullable = false, length = 100)
    private String hall;

    @NotNull(message = "Available seats is mandatory")
    @Min(value = 1, message = "Available seats must be at least 1")
    @Column(nullable = false)
    private Integer availableSeats;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ticket> tickets;
}
