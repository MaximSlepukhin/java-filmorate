package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(Integer id);

    List<Genre> getGenres();

   Set<Genre> addGenresForFilm(Set<Genre> genresFromFilm, Integer id);

    Set<Genre> updateGenresForFilm(Set<Genre> genresFromFilm, Integer id);

    Set<Genre> getGenresForFilmList(Integer filmId);
}
