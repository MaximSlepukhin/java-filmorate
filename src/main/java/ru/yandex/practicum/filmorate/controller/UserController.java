package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private Map<Integer, User> users = new HashMap<>();

    private int userId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if(user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isBefore(LocalDate.now())) {
            user.setId(userId++);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Неверная дата");
        }
        log.debug("Получен запрос POST /user");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Данного пользоватля не существует");
        }
        log.info("Пользователь обновлен на {}", user);
        return user;
    }

    @GetMapping
    @Valid
    public Collection<User> getUsers() {
        log.info("Возвращен список фильмов");
        return users.values();

    }
}
