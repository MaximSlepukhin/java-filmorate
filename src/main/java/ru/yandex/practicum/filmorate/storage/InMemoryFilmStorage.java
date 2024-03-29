package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film postFilm(Film film) {
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Данного фильма не существует");
        }
        log.info("Фильм обновлен на {}", film);
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        log.info("Возвращен список фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(Integer id) {
        Collection<Film> films = getFilms();
        Optional<Film> film = films.stream()
                .filter(film1 -> film1.getId() == id)
                .findFirst();
        return film.orElse(null);
    }
}
