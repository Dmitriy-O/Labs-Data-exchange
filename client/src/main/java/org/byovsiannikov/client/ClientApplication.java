package org.byovsiannikov.client;

import org.byovsiannikov.server.model.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientApplication {

    private static final String BASE_URL = "http://localhost:8080/movies";
    private RestTemplate restTemplate;
    private Scanner scanner;

    public ClientApplication() {
        this.restTemplate = new RestTemplate();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ClientApplication client = new ClientApplication();
        client.run();
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    getAllMovies();
                    break;
                case 3:
                    getMovieById();
                    break;
                case 4:
                    updateMovie();
                    break;
                case 5:
                    deleteMovie();
                    break;
                case 6:
                    System.out.println("Вихід з програми.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== Клієнт REST API ===");
        System.out.println("1. Додати новий фільм");
        System.out.println("2. Отримати список всіх фільмів");
        System.out.println("3. Отримати фільм за ID");
        System.out.println("4. Оновити фільм");
        System.out.println("5. Видалити фільм");
        System.out.println("6. Вийти");
        System.out.print("Виберіть опцію: ");
    }

    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void addMovie() {
        System.out.println("\n=== Додати Новий Фільм ===");
        Movie movie = new Movie();
        System.out.print("Назва фільму: ");
        movie.setTitle(scanner.nextLine());
        System.out.print("Жанр фільму: ");
        movie.setGenre(scanner.nextLine());
        System.out.print("Тривалість (хвилин): ");
        try {
            movie.setDuration(Integer.parseInt(scanner.nextLine()));
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат тривалості.");
            return;
        }

        try {
            ResponseEntity<Movie> response = restTemplate.postForEntity(BASE_URL, movie, Movie.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Фільм успішно додано: " + response.getBody());
            } else {
                System.out.println("Помилка при додаванні фільму: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }
    }

    private void getAllMovies() {
        System.out.println("\n=== Список Всіх Фільмів ===");
        try {
            ResponseEntity<Movie[]> response = restTemplate.getForEntity(BASE_URL, Movie[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Movie> movies = Arrays.asList(response.getBody());
                movies.forEach(System.out::println);
            } else {
                System.out.println("Помилка при отриманні фільмів: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }
    }

    private void getMovieById() {
        System.out.println("\n=== Отримати Фільм за ID ===");
        System.out.print("Введіть ID фільму: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID.");
            return;
        }

        String url = BASE_URL + "/" + id;
        try {
            ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Фільм: " + response.getBody());
            } else {
                System.out.println("Фільм не знайдено: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }
    }

    private void updateMovie() {
        System.out.println("\n=== Оновити Фільм ===");
        System.out.print("Введіть ID фільму для оновлення: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID.");
            return;
        }

        Movie updatedMovie = new Movie();
        System.out.print("Нова назва фільму: ");
        updatedMovie.setTitle(scanner.nextLine());
        System.out.print("Новий жанр фільму: ");
        updatedMovie.setGenre(scanner.nextLine());
        System.out.print("Нова тривалість (хвилин): ");
        try {
            updatedMovie.setDuration(Integer.parseInt(scanner.nextLine()));
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат тривалості.");
            return;
        }

        String url = BASE_URL + "/" + id;
        try {
            restTemplate.put(url, updatedMovie);
            System.out.println("Фільм успішно оновлено.");
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }
    }

    private void deleteMovie() {
        System.out.println("\n=== Видалити Фільм ===");
        System.out.print("Введіть ID фільму для видалення: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID.");
            return;
        }

        String url = BASE_URL + "/" + id;
        try {
            restTemplate.delete(url);
            System.out.println("Фільм успішно видалено.");
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
        }
    }
}
