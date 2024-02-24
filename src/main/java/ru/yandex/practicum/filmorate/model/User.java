package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {

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

    //private Set<Integer> unapprovedFriends = new TreeSet<>();

    //добавил поле с фильмами, которые лайкнул пользователь
    private Set<Integer> likedFilms;
}
