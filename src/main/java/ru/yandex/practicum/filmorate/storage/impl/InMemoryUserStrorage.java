package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStrorage implements UserStorage {
    public Map<Integer, User> users = new TreeMap<>();

    private int userId = 1;

    @Override
    public User createUser(User user) {
        log.debug("Получен запрос POST /user");
        user.setFriends(new HashSet<>());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isBefore(LocalDate.now())) {
            user.setId(userId++);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Неверная дата");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Данного пользоватля не существует");
        }
        log.info("Пользователь обновлен на {}", user);
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Возвращен список пользователей");
        return users.values();
    }

    @Override
    public User findUserById(Integer id) {
        Collection<User> users = getUsers();
        Optional<User> user = users.stream()
                .filter(user1 -> user1.getId() == id)
                .findFirst();
        return user.orElse(null);
    }
}
