package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(Integer id, Integer friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("id равен friendId");
        }
        userStorage.deleteFriend(id, friendId);
    }

    public void checkIfUserExists(Integer id) {
        boolean isExist = userStorage.getUsers().stream().anyMatch(user -> user.getId() == id);
        if (!isExist) {
            throw new UserNotFoundException("Пользователя c id=" + id + " не сушествует");
        }
    }

    public List<User> getListOfFriends(Integer id) {
        return userStorage.getListOfFriends(id);
    }

    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
        return userStorage.getListOfCommonFriends(id, otherId);
    }

    public User findUserById(Integer id) {
        checkIfUserExists(id);
        return userStorage.findUserById(id);
    }
}



