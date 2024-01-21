package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
@Service
public class FilmController {

    private final FilmService filmService;
    public final static Integer TOP_10_FILM = 10;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws IllegalAccessException {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void like(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.like(id, userId);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public Collection<Film> topFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.topFilms(count.get());
        } else {
            return filmService.topFilms(TOP_10_FILM);
        }
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }
}

