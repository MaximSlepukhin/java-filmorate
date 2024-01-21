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

    public final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id, Integer friendId) {
        checkId(id);
        checkId(friendId);

        User userOne = findUserById(id);
        User userTwo = findUserById(friendId);

        userOne.friends.add(friendId);
        userTwo.friends.add(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("id равен friendId");
        }
        checkId(id);
        checkId(friendId);

        User userOne = findUserById(id);
        User userTwo = findUserById(friendId);

        userOne.friends.remove(userTwo.getId());
        userTwo.friends.remove(userOne.getId());

    }
    public void checkId(Integer id) {
        boolean isExist = userStorage.getUsers().stream().anyMatch(user -> user.getId() == id);
        if (!isExist) {
            throw new UserNotFoundException("Пользователя c id=" + id + " не сушествует");
        }
    }

    public Set<User> friendsList(Integer id) {
        checkId(id);
        Set<User> friendList = userStorage.getUsers().stream()
                .filter(u -> findUserById(id).friends.contains(u.getId()))
                .collect(Collectors.toSet());
        Set<User> result = new TreeSet<>(Comparator.comparingInt(User::getId));
        result.addAll(friendList);
        return result;
    }

    public List<User> listOfCommonFriends(Integer id, Integer otherId) {
        checkId(id);
        checkId(otherId);
        return friendsList(id).stream()
                .filter(f -> friendsList(otherId).contains(f))
                .collect(Collectors.toList());
    }

    public User findUserById(Integer id) {
        checkId(id);
        Collection<User> users = userStorage.getUsers();
        Optional<User> user = users.stream()
                .filter(user1 -> user1.getId() == id)
                .findFirst();
        return user.orElse(null);

    }
}



