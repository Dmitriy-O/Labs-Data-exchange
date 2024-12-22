package org.byovsiannikov.server.controller;

import jakarta.validation.Valid;
import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Movie;
import org.byovsiannikov.server.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired // Ін'єкція залежності через конструктор
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Створення нового фільму
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.ok(createdMovie);
    }

    // Отримання списку всіх фільмів
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // Отримання фільму за ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фільм не знайдено з ID: " + id));
        return ResponseEntity.ok(movie);
    }

    // Оновлення фільму
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updatedMovie);
    }

    // Видалення фільму
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
