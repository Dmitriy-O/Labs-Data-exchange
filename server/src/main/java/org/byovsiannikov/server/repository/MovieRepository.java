package org.byovsiannikov.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.byovsiannikov.server.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Додаткові методи, якщо необхідно
    // Наприклад, пошук фільмів за жанром
    // List<Movie> findByGenre(String genre);
}