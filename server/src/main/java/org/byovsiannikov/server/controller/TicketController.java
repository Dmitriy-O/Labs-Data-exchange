package org.byovsiannikov.server.controller;

import jakarta.validation.Valid;
import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Ticket;
import org.byovsiannikov.server.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Бронювання нового квитка

    @PostMapping
    public ResponseEntity<Ticket> bookTicket(@Valid @RequestBody Ticket ticket) {
        Ticket bookedTicket = ticketService.bookTicket(ticket);
        return ResponseEntity.ok(bookedTicket);
    }

    // Отримання списку всіх квитків
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    // Отримання квитка за ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Квиток не знайдено з ID: " + id));
        return ResponseEntity.ok(ticket);
    }

    // Скасування квитка
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    // Оновлення квитка (якщо необхідно)
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticketDetails) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticketDetails);
        return ResponseEntity.ok(updatedTicket);
    }
}
