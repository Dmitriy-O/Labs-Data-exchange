package org.byovsiannikov.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`") // Використовуйте екранування, оскільки "order" є зарезервованим словом
public class Order {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Popcorn popcorn;

    @ManyToOne(fetch = FetchType.EAGER)
    private Film film;

    private Integer quantity;
    private Float totalPrice;
    private String orderDate;
}