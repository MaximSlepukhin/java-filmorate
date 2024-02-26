package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film postFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getFilms();

    Film getFilmById(Integer id);

    Collection<Film> getPopularFilms(Integer limit);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);


}
