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

    public int id;

    @NotBlank
    public String name;

    @Size(min = 0, max = 200)
    public String description;

    public LocalDate releaseDate;

    @Positive
    public Integer duration;

    public Set<Integer> likes = new TreeSet<>();

    public List<Film> film;
}
