package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film {

    private static final Integer LENGTH_NAME = 200;// Можно будет перенсти

    private static final LocalDate START_TIME = LocalDate.of(1895, 12, 28);// Можно будет перенсти

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isEmpty() || name.isBlank()) {//null
            this.name = name;
        } else {
            throw new ValidationException("Пустое имя");
        }
    }

    public void setDescription(String description) {
        if (description.length() <= LENGTH_NAME) {
            this.description = description;
        } else {
            throw new ValidationException("Длина описания больше" + LENGTH_NAME);
        }
    }

    public void setReleaseDate(LocalDate releaseDate) {
        if (!releaseDate.isBefore(START_TIME)) {
            this.releaseDate = releaseDate;
        } else {
            throw new ValidationException("Дата релиза раньше " + START_TIME);
        }
    }

    public void setDuration(Integer duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            throw new ValidationException("Отрицательное число");
        }
    }

    public void chekTrue(Film film) {
        setName(film.getName());
        setDescription(film.getDescription());
        setReleaseDate(film.getReleaseDate());
        setDuration(film.getDuration());
    }
}
