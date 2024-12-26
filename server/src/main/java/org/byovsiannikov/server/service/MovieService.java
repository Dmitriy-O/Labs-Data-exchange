package org.byovsiannikov.server.service;

import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Movie;
import org.byovsiannikov.server.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Створення нового фільму
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Отримання всіх фільмів
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Отримання фільму за ID
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Оновлення фільму
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фільм не знайдено з ID: " + id));

        movie.setTitle(movieDetails.getTitle());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());

        return movieRepository.save(movie);
    }

    // Видалення фільму
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Фільм не знайдено з ID: " + id));
        movieRepository.delete(movie);
    }
}
