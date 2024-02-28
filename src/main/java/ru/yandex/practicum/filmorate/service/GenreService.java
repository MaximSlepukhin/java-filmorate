package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Set;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(Integer id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Set<Genre> addGenresForFilm(Set<Genre> genresFromFilm, Integer id) {
        return genreStorage.addGenresForFilm(genresFromFilm, id);
    }

    public Set<Genre> updateGenresForFilm(Set<Genre> genresFromFilm, Integer id) {
        return genreStorage.updateGenresForFilm(genresFromFilm, id);
    }

    Set<Genre> getGenresForFilmList(Integer filmId) {
        return genreStorage.setGenresForFilmList(filmId);
    }
}
