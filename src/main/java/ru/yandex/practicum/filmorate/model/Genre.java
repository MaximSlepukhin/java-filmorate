package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Genre implements Comparable<Genre> {

    private int id;

    private String name;

    @Override
    public int compareTo(Genre o) {
        return id - o.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
