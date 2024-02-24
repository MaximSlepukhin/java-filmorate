package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

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

    private MPARating mpa;

    private Set<Integer> likes;

    private Set<Genre> genres;

}
