package org.byovsiannikov.server.service;

import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Movie;
import org.byovsiannikov.server.model.Screening;
import org.byovsiannikov.server.repository.MovieRepository;
import org.byovsiannikov.server.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
    }

    // Створення нового сеансу
    public Screening createScreening(Screening screening) {
        Movie movie = movieRepository.findById(screening.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Фільм не знайдено з ID: " + screening.getMovie().getId()));
        screening.setMovie(movie);
        return screeningRepository.save(screening);
    }

    // Отримання всіх сеансів
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    // Отримання сеансу за ID
    public Optional<Screening> getScreeningById(Long id) {
        return screeningRepository.findById(id);
    }

    // Оновлення сеансу
    public Screening updateScreening(Long id, Screening screeningDetails) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сеанс не знайдено з ID: " + id));

        Movie movie = movieRepository.findById(screeningDetails.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Фільм не знайдено з ID: " + screeningDetails.getMovie().getId()));

        screening.setMovie(movie);
        screening.setStartTime(screeningDetails.getStartTime());
        screening.setHall(screeningDetails.getHall());
        screening.setAvailableSeats(screeningDetails.getAvailableSeats());

        return screeningRepository.save(screening);
    }

    // Видалення сеансу
    public void deleteScreening(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сеанс не знайдено з ID: " + id));
        screeningRepository.delete(screening);
    }
}
