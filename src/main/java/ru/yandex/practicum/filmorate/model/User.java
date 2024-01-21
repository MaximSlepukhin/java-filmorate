package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    private LocalDate birthday;
    public Set<Integer> friends = new TreeSet<>();
}
