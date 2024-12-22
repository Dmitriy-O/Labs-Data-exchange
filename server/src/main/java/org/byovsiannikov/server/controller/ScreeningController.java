package org.byovsiannikov.server.controller;

import jakarta.validation.Valid;
import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Screening;
import org.byovsiannikov.server.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    @Autowired
    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    // Створення нового сеансу
    @PostMapping
    public ResponseEntity<Screening> createScreening(@Valid @RequestBody Screening screening) {
        Screening createdScreening = screeningService.createScreening(screening);
        return ResponseEntity.ok(createdScreening);
    }
    // Отримання списку всіх сеансів
    @GetMapping
    public ResponseEntity<List<Screening>> getAllScreenings() {
        List<Screening> screenings = screeningService.getAllScreenings();
        return ResponseEntity.ok(screenings);
    }

    // Отримання сеансу за ID
    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(@PathVariable Long id) {
        Screening screening = screeningService.getScreeningById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сеанс не знайдено з ID: " + id));
        return ResponseEntity.ok(screening);
    }

    // Оновлення сеансу
    @PutMapping("/{id}")
    public ResponseEntity<Screening> updateScreening(@PathVariable Long id, @Valid @RequestBody Screening screeningDetails) {
        Screening updatedScreening = screeningService.updateScreening(id, screeningDetails);
        return ResponseEntity.ok(updatedScreening);
    }

    // Видалення сеансу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
    }
}
