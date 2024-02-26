package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    User findUserById(Integer id);

    List<User> getListOfFriends(Integer id);

    List<User> getListOfCommonFriends(Integer id, Integer otherId);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

}
