package org.byovsiannikov.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets") // Додаємо @Table для відповідності data.sql
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @NotNull(message = "Seat number is mandatory")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @NotBlank(message = "Customer name is mandatory")
    @Size(max = 100, message = "Customer name must be at most 100 characters")
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
}
