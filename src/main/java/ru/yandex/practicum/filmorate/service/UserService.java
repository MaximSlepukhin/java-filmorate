package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

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
        checkIfUserExists(id);
        checkIfUserExists(friendId);

        User userOne = findUserById(id);
        User userTwo = findUserById(friendId);

        userOne.getFriends().add(friendId);
        userTwo.getFriends().add(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("id равен friendId");
        }
        checkIfUserExists(id);
        checkIfUserExists(friendId);

        User firstUser = findUserById(id);
        User secondUser = findUserById(friendId);

        firstUser.getFriends().remove(secondUser.getId());
        secondUser.getFriends().remove(firstUser.getId());

    }

    public void checkIfUserExists(Integer id) {
        boolean isExist = userStorage.getUsers().stream().anyMatch(user -> user.getId() == id);
        if (!isExist) {
            throw new UserNotFoundException("Пользователя c id=" + id + " не сушествует");
        }
    }

    public Set<User> getListOfFriends(Integer id) {
        checkIfUserExists(id);
        Set<User> friendList = userStorage.getUsers().stream()
                .filter(u -> findUserById(id).getFriends().contains(u.getId()))
                .collect(Collectors.toSet());
        Set<User> result = new TreeSet<>(Comparator.comparingInt(User::getId));
        result.addAll(friendList);
        return result;
    }

    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
        checkIfUserExists(id);
        checkIfUserExists(otherId);
        return getListOfFriends(id).stream()
                .filter(f -> getListOfFriends(otherId).contains(f))
                .collect(Collectors.toList());
    }

    public User findUserById(Integer id) {
        checkIfUserExists(id);
        return userStorage.findUserById(id);
    }
}



