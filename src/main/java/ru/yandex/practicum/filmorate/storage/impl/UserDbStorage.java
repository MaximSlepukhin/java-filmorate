package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "INSERT INTO users (login, user_name, email, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int userId = keyHolder.getKey().intValue();
        user.setId(userId);
        user.setFriends(new HashSet<>());
        user.setLikedFilms(new HashSet<>());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? " +
                "WHERE user_id = ?";
        int updatRows = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (updatRows == 0) {
            throw new UserNotFoundException("Ползователь с id = " + user.getId() + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapUser(rs));
        for (User user : users) {
            String likesQuery = "SELECT * FROM likes WHERE user_id = ?";
            List<Integer> likes = jdbcTemplate.query(likesQuery, (rs, rowNum)
                    -> rs.getInt("film_id"), user.getId());
            user.setLikedFilms(new HashSet<>(likes));
            user.setFriends(new HashSet<>());
        }
        return users;
    }

    @Override
    public User findUserById(Integer id) {
        String userQuery = "SELECT * " +
                "FROM users AS u " +
                "WHERE u.user_id = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(userQuery, (rs, a) -> mapUser(rs), id);
        } catch (DataAccessException e) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        user.setLikedFilms(new HashSet<>());
        user.setFriends(new HashSet<>());
        return user;
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        String sqlQuery = "SELECT u.user_id, u.email, u.login, u.user_name, u.birthday " +
                "FROM users AS u " +
                "JOIN friendship AS f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ?";
        List<User> listOfFriends = jdbcTemplate.query(sqlQuery, (rs, a) -> mapUser(rs), id);
        return listOfFriends;
    }

    @Override
    public List<User> getListOfCommonFriends(Integer id, Integer otherId) {
    //дорабатываю метод
        return null;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new UserNotFoundException("Отрицательный id пользователя");
        }
        String sqlQuery = "insert into friendship (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(1));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }

    private Integer mapLikes(ResultSet rs) throws SQLException {
        return rs.getInt("film_id");
    }
}



