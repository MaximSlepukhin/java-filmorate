package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private List<User> users = new ArrayList<>();

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (!user.getEmail().isEmpty()
                && user.getEmail().contains("@")
                && user.getLogin().isBlank()
                && !user.getLogin().contains(" ")
                && !user.getBirthday().isAfter(LocalDate.now())) {
            users.add(user);
            log.debug("Получен запрос POST /user");
        } else {
            log.debug("Данные в запросе не соответствуют критериям");
        }
        return user;
    }
}
