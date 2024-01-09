package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmControllerTests {


    @Test
    public void postFilmShouldAddFilm() {
        Film film = new Film();

        film.setName("");
    }
}
