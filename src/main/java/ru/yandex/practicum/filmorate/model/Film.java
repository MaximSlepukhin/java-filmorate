package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.HistoricalReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film {

    public int id;

    @NotBlank
    public String name;

    @Size(min = 0, max = 200)
    public String description;

    @HistoricalReleaseDate
    public LocalDate releaseDate;

    @Positive
    public Integer duration;

}
