package org.byovsiannikov.server.repository;

import org.byovsiannikov.server.model.Popcorn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopcornRepository extends JpaRepository<Popcorn, String> {
}