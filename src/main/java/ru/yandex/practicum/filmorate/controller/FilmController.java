package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
 //   private static final Logger log = LoggerFactory.getLogger(FilmController.class);// Можно использовать анотацию
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film postFilm(@RequestBody Film film) {// Можно пределать или в сет и здесь его вызывать или с помощью анотаций делать проверки
          film.chekTrue(film);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film); // Коректней будет выглядить ""
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Данного фильма не существует");
        }
        log.info("Фильм обновлен на {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Возвращен список фильмов");
        return (List<Film>) films.values(); // Доделать

    }


}
