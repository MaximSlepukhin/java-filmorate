package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Запрос на получение списка жанров");
        return genreService.getGenres();
    }

    @GetMapping("{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Запрос на получение жанра по идентификатору {} принят", id);
        return genreService.getGenreById(id);
    }
}
