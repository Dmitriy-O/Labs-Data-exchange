package org.byovsiannikov.server.repository;

import org.byovsiannikov.server.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, String> {
}