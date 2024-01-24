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
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        log.info("Что-то пошло не так");
        userService.addFriend(Integer.valueOf(id), Integer.valueOf(friendId));
    }

    @GetMapping
    @Valid
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{id}/friends")
    public Set<User> friendsList(@PathVariable Integer id) {
        return userService.friendsList(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> listOfCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.listOfCommonFriends(id, otherId);
    }

    @GetMapping("{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (friendId == null && id == null) {
            throw new IncorrectCountException("Параметр count равен null.");
        }
        if (friendId <= 0 && id == null) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }
    }
}
