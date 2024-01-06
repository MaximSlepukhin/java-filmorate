package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTests {

    Film film = new Film();

    @Test
    void shouldNameBeNotEmpty() {
        String excepted = "Avatar";
        film.setName(excepted);
        String actual = film.getName();

        assertEquals(excepted, actual);


    }
}
