package org.byovsiannikov.server.repository;

import org.byovsiannikov.server.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByScreeningIdAndSeatNumber(Long screeningId, Integer seatNumber);
}
