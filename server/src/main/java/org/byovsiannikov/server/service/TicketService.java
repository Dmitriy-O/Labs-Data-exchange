package org.byovsiannikov.server.service;

import org.byovsiannikov.server.exception.ResourceNotFoundException;
import org.byovsiannikov.server.model.Screening;
import org.byovsiannikov.server.model.Ticket;
import org.byovsiannikov.server.repository.ScreeningRepository;
import org.byovsiannikov.server.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, ScreeningRepository screeningRepository) {
        this.ticketRepository = ticketRepository;
        this.screeningRepository = screeningRepository;
    }

    // Бронювання нового квитка
    public Ticket bookTicket(Ticket ticket) {
        Screening screening = screeningRepository.findById(ticket.getScreening().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Сеанс не знайдено з ID: " + ticket.getScreening().getId()));

        if (screening.getAvailableSeats() < 1) {
            throw new IllegalArgumentException("Вільних місць немає");
        }

        // Перевірка, чи вже заброньовано це місце
        boolean seatTaken = ticketRepository.existsByScreeningIdAndSeatNumber(ticket.getScreening().getId(), ticket.getSeatNumber());
        if (seatTaken) {
            throw new IllegalArgumentException("Місце вже заброньовано");
        }

        screening.setAvailableSeats(screening.getAvailableSeats() - 1);
        screeningRepository.save(screening);

        return ticketRepository.save(ticket);
    }

    // Отримання всіх квитків
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Отримання квитка за ID
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    // Оновлення квитка
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Квиток не знайдено з ID: " + id));

        Screening screening = screeningRepository.findById(ticketDetails.getScreening().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Сеанс не знайдено з ID: " + ticketDetails.getScreening().getId()));

        // Перевірка, чи місце вже заброньовано, якщо змінився номер місця
        if (!ticket.getSeatNumber().equals(ticketDetails.getSeatNumber())) {
            boolean seatTaken = ticketRepository.existsByScreeningIdAndSeatNumber(screening.getId(), ticketDetails.getSeatNumber());
            if (seatTaken) {
                throw new IllegalArgumentException("Місце вже заброньовано");
            }
            // Повертаємо попереднє місце
            screening.setAvailableSeats(screening.getAvailableSeats() + 1);
        }

        // Оновлюємо інформацію про квиток
        ticket.setScreening(screening);
        ticket.setSeatNumber(ticketDetails.getSeatNumber());
        ticket.setCustomerName(ticketDetails.getCustomerName());

        screening.setAvailableSeats(screening.getAvailableSeats() - 1);
        screeningRepository.save(screening);

        return ticketRepository.save(ticket);
    }

    // Скасування квитка
    public void cancelTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Квиток не знайдено з ID: " + id));
        Screening screening = ticket.getScreening();
        screening.setAvailableSeats(screening.getAvailableSeats() + 1);
        screeningRepository.save(screening);
        ticketRepository.delete(ticket);
    }
}
