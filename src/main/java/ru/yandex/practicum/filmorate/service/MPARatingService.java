package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.util.List;

@Service
public class MPARatingService {
    private final MPARatingStorage mpaRatingStorage;

    public MPARatingService(MPARatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public MPARating getMPARatingById(Integer id) {
        return mpaRatingStorage.getMPARatingById(id);
    }

    public List<MPARating> getMPARatings() {
        return mpaRatingStorage.getMPARatings();
    }
}
