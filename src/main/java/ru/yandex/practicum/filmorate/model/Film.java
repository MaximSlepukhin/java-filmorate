package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
public class Film {

    private int id;

    @NotBlank
    private String name;

    @Size(min = 0, max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private Set<Integer> likes = new TreeSet<>();

    private List<Film> film;
}
