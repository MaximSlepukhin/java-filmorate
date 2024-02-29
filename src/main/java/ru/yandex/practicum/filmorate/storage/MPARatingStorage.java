package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

public interface MPARatingStorage {

    MPARating getMPARatingById(Integer id);

    List<MPARating> getMPARatings();
}
