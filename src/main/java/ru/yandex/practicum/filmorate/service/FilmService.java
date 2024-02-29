package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

@Service
public class FilmService {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    private final UserService userService;

    private final GenreService genreService;

    private Integer count = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Невозможно добавить фильм с датой релиза фильма ранее " + MIN_DATE_RELEASE);
        }
        Film addedFilm = filmStorage.addFilm(film);
        if (film.getGenres() != null) {
           genreService.addGenresForFilm(film.getGenres(), addedFilm.getId());

        }
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        checkFilmExists(film.getId());
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Невозможно добавить фильм с датой релиза фильма ранее " + MIN_DATE_RELEASE);
        }
        Film updatedFilm = filmStorage.updateFilm(film);
        Set<Genre> updatedGenre = new TreeSet<>();
        if (film.getGenres() != null) {
            updatedGenre = genreService.updateGenresForFilm(film.getGenres(), film.getId());
        }
        updatedFilm.setGenres(updatedGenre);
        return updatedFilm;
    }

    public Collection<Film> getFilms() {
        Collection<Film> listFilms = filmStorage.getFilms();
        for (Film film : listFilms) {
            film.setGenres(genreService.getGenresForFilmList(film.getId()));
        }
        return listFilms;
    }

    public void addLike(Integer filmId, Integer userId) {
        checkFilmExists(filmId);
        userService.checkUserExists(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        checkFilmExists(filmId);
        userService.checkUserExists(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Film findFilmById(Integer id) {
        Film addedFilm = filmStorage.getFilmById(id);
        addedFilm.setGenres(genreService.getGenresForFilmList(addedFilm.getId()));
        return addedFilm;
    }

    public Collection<Film> topFilms(Integer count) {
        Collection<Film> listFilms = filmStorage.getPopularFilms(count);
        for (Film film : listFilms) {
            film.setGenres(genreService.getGenresForFilmList(film.getId()));
        }
        return listFilms;
    }

    public void checkFilmExists(Integer id) {
        Film film = findFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }
}
