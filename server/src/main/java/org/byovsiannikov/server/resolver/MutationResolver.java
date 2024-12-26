package org.byovsiannikov.server.resolver;

import org.byovsiannikov.server.model.Film;
import org.byovsiannikov.server.model.Order;
import org.byovsiannikov.server.model.Popcorn;
import org.byovsiannikov.server.service.FilmService;
import org.byovsiannikov.server.service.OrderService;
import org.byovsiannikov.server.service.PopcornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Controller
public class MutationResolver {

    private static final Logger logger = Logger.getLogger(MutationResolver.class.getName());

    private final FilmService filmService;
    private final PopcornService popcornService;
    private final OrderService orderService;

    @Autowired
    public MutationResolver(FilmService filmService, PopcornService popcornService, OrderService orderService) {
        this.filmService = filmService;
        this.popcornService = popcornService;
        this.orderService = orderService;
    }

    @MutationMapping
    public Film addFilm(
            @Argument String title,
            @Argument String genre,
            @Argument String releaseDate,
            @Argument int duration,
            @Argument Float price // Додане поле ціни
    ) {
        logger.info("Adding film: " + title);
        Film film = new Film();
        film.setId(UUID.randomUUID().toString());
        film.setTitle(title);
        film.setGenre(genre);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        film.setPrice(price); // Встановлення ціни
        return filmService.addFilm(film);
    }

    @MutationMapping
    public Film updateFilm(
            @Argument String id,
            @Argument String title,
            @Argument String genre,
            @Argument String releaseDate,
            @Argument Integer duration,
            @Argument Float price // Додане поле ціни
    ) {
        logger.info("Updating film with ID: " + id);
        Optional<Film> filmOpt = filmService.getFilmById(id);
        if (filmOpt.isPresent()) {
            Film film = filmOpt.get();
            if (title != null) film.setTitle(title);
            if (genre != null) film.setGenre(genre);
            if (releaseDate != null) film.setReleaseDate(releaseDate);
            if (duration != null) film.setDuration(duration);
            if (price != null) film.setPrice(price); // Оновлення ціни
            return filmService.addFilm(film);
        } else {
            throw new IllegalArgumentException("Film with ID " + id + " not found.");
        }
    }

    @MutationMapping
    public Popcorn addPopcorn(
            @Argument String size,
            @Argument Float price
    ) {
        logger.info("Adding popcorn: " + size + " - $" + price);
        Popcorn popcorn = new Popcorn();
        popcorn.setId(UUID.randomUUID().toString());
        popcorn.setSize(size);
        popcorn.setPrice(price);
        return popcornService.addPopcorn(popcorn);
    }

    @MutationMapping
    public Order placeOrder(
            @Argument String popcornId,
            @Argument String filmId,
            @Argument int quantity
    ) {
        logger.info("Placing order: popcornId=" + popcornId + ", filmId=" + filmId + ", quantity=" + quantity);
        Popcorn popcorn = popcornService.getPopcornById(popcornId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Popcorn ID"));
        Film film = filmService.getFilmById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Film ID"));
        float totalPrice = popcorn.getPrice() * quantity + film.getPrice(); // Припустимо, ціна фільму додається до замовлення
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setPopcorn(popcorn);
        order.setFilm(film);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(java.time.LocalDate.now().toString());
        return orderService.placeOrder(order);
    }
}