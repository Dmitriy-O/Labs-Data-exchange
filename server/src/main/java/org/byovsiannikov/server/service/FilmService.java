package org.byovsiannikov.server.service;

import org.byovsiannikov.server.model.Film;
import org.byovsiannikov.server.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film addFilm(Film film) {
        return filmRepository.save(film);
    }

    public Optional<Film> getFilmById(String id) {
        return filmRepository.findById(id);
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    // Додаткові методи (оновлення, видалення) за потребою
}