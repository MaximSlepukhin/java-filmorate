package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MPARatingController {

    private final MPARatingService mpaRatingService;

    public MPARatingController(MPARatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public List<MPARating> getRatings() {
        log.info("Запрос на получение списка с рейтингом фильмов");
        return mpaRatingService.getMPARatings();
    }

    @GetMapping("{id}")
    public MPARating getMPARatingById(@PathVariable Integer id) {
        log.info("Запрос на получение рейтинга по идентификатору {} принят", id);
        return mpaRatingService.getMPARatingById(id);
    }
}