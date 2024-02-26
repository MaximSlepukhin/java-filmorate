package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectCountException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя принят.");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя принят.");
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Запрс на добавление в друзья принят.");
        userService.addFriend(Integer.valueOf(id), Integer.valueOf(friendId));
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрс на получение списка пользователей принят.");
        return userService.getUsers();
    }

    @GetMapping("{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        log.info("Запрс на получение списка друзей принят.");
        return userService.getListOfFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListOfCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Запрос на получение общего списка друзей принят.");
        return userService.getListOfCommonFriends(id, otherId);
    }

    @GetMapping("{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("Запрос на получение пользователя по id принят.");
        return userService.findUserById(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Запрс на удаления пользователя из списка друзей принят.");
        if (friendId == null && id == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (friendId <= 0 && id == null) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }
        userService.deleteFriend(id, friendId);
    }
}
