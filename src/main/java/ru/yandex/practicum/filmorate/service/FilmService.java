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
import java.util.stream.Collectors;

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

    public Film postFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Невозможно добавить фильм с датой релиза фильма ранее " + MIN_DATE_RELEASE);
        }
        Film addedFilm = filmStorage.postFilm(film);
        if (film.getGenres() != null) {
            genreService.addGenresForFilm(film.getGenres(), addedFilm.getId());
        }
        return addedFilm;
    }

    public Film getFilmById(Integer id) {
        //checkIfFilmExists(id);
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Невозможно добавить фильм с датой релиза фильма ранее " + MIN_DATE_RELEASE);
        }
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(Integer filmId, Integer userId) {
        userService.checkIfUserExists(userId);
        Film film = findFilmById(filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userService.checkIfUserExists(userId);
        Film film = findFilmById(filmId);
        film.getLikes().remove(userId);
    }

    public Film findFilmById(Integer id) {
        /*checkIfFilmExists(id);
        Collection<Film> films = filmStorage.getFilms();
        Optional<Film> film = films.stream()
                .filter(film1 -> film1.getId() == id)
                .findFirst();

        return film.orElse(null);*/

        return filmStorage.getFilmById(id);
    }

    public Collection<Film> topFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void checkIfFilmExists(Integer id) {
        boolean isExist = filmStorage.getFilms().stream().anyMatch(film -> film.getId() == id);
        if (!isExist) {
            throw new FilmNotFoundException("Фильма c id=" + id + " не сушествует");
        }
    }
}
