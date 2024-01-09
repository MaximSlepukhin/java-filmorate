package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotations.HistoricalReleaseDateValidator;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j

public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;


    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) throws IllegalAccessException {
        log.info("Пришел запрос на добавление фильма");
        HistoricalReleaseDateValidator.validate(film);
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws IllegalAccessException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Данного фильма не существует");
        }
        log.info("Фильм обновлен на {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Возвращен список фильмов");
        return films.values();
    }
}
