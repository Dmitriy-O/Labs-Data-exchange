package org.byovsiannikov.server.service;

import org.byovsiannikov.server.model.Popcorn;
import org.byovsiannikov.server.repository.PopcornRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PopcornService {

    private final PopcornRepository popcornRepository;

    @Autowired
    public PopcornService(PopcornRepository popcornRepository) {
        this.popcornRepository = popcornRepository;
    }

    public Popcorn addPopcorn(Popcorn popcorn) {
        return popcornRepository.save(popcorn);
    }

    public Optional<Popcorn> getPopcornById(String id) {
        return popcornRepository.findById(id);
    }

    public List<Popcorn> getAllPopcorns() {
        return popcornRepository.findAll();
    }

    // Додаткові методи (оновлення, видалення) за потребою
}