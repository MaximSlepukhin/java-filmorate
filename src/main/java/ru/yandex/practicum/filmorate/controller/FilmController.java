package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Запрос на добавление фильма принят.");
        return filmService.postFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws IllegalAccessException {
        log.info("Запрос на обновление фильма принят.");
        return filmService.updateFilm(film);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Запрос на добавление лайка фильму принят.");
        filmService.addLike(id, userId);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрос на получение списка фильмов принят.");
        return filmService.getFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> topFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Запрос на получение списка самых популярных фильмов принят.");
        return filmService.topFilms(count);
    }

    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Запрос на получение фильма по id принят.");
        return filmService.findFilmById(id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Запрос на удаление лайка принят.");
        filmService.deleteLike(id, userId);
    }
}

