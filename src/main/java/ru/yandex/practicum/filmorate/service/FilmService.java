package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    Integer count = 0;

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    public final FilmStorage filmStorage;
    public final UserStorage userStorage;
    public final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    public Film postFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Невозможно добавить фильм с датой релиза фильма ранее " + MIN_DATE_RELEASE);
        }
        return filmStorage.postFilm(film);
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
        Integer idOfUser = userService.findUserById(userId).getId();
        Film film = findFilmById(filmId);
        film.likes.add(idOfUser);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Integer idOfUser = userService.findUserById(userId).getId();
        Film film = findFilmById(filmId);
        film.likes.remove(idOfUser);
    }

    public Film findFilmById(Integer id) {
        Collection<Film> films = filmStorage.getFilms();
        Optional<Film> film = films.stream()
                .filter(film1 -> film1.getId() == id)
                .findFirst();
        return film.orElse(null);
    }

    public Collection<Film> topFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        checkFilmId(id);
        Collection<Film> films = filmStorage.getFilms();
        Optional<Film> film = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
        return film.orElse(null);
    }

    public void checkFilmId(Integer id) {
        boolean isExist = filmStorage.getFilms().stream().anyMatch(film -> film.getId() == id);
        if (!isExist) {
            throw new FilmNotFoundException("Фильма c id=" + id + " не сушествует");
        }
    }
}