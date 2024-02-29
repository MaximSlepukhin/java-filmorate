package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {
    @Positive
    private Integer id;

    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;
    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Integer> friends = new TreeSet<>();

    private Set<Integer> likedFilms;
}
