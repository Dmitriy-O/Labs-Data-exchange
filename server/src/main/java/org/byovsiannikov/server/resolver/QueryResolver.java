package org.byovsiannikov.server.resolver;

import org.byovsiannikov.server.model.Film;
import org.byovsiannikov.server.model.Order;
import org.byovsiannikov.server.model.Popcorn;
import org.byovsiannikov.server.service.FilmService;
import org.byovsiannikov.server.service.OrderService;
import org.byovsiannikov.server.service.PopcornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class QueryResolver {

    private static final Logger logger = Logger.getLogger(QueryResolver.class.getName());

    private final FilmService filmService;
    private final PopcornService popcornService;
    private final OrderService orderService;

    @Autowired
    public QueryResolver(FilmService filmService, PopcornService popcornService, OrderService orderService) {
        this.filmService = filmService;
        this.popcornService = popcornService;
        this.orderService = orderService;
    }

    @QueryMapping
    public List<Film> getAllFilms() {
        logger.info("Fetching all films");
        return filmService.getAllFilms();
    }

    @QueryMapping
    public Film getFilmById(@Argument String id) {
        logger.info("Fetching film by ID: " + id);
        return filmService.getFilmById(id).orElse(null);
    }

    @QueryMapping
    public List<Popcorn> getAllPopcorns() {
        logger.info("Fetching all popcorns");
        return popcornService.getAllPopcorns();
    }

    @QueryMapping
    public Popcorn getPopcornById(@Argument String id) {
        logger.info("Fetching popcorn by ID: " + id);
        return popcornService.getPopcornById(id).orElse(null);
    }

    @QueryMapping
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderService.getAllOrders();
    }

    @QueryMapping
    public Order getOrderById(@Argument String id) {
        logger.info("Fetching order by ID: " + id);
        return orderService.getOrderById(id).orElse(null);
    }
}