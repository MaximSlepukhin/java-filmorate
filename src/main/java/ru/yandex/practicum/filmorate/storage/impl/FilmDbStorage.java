package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO film (film_name, description, release_date, duration, " +
                "mpa_id) " + "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int filmId = keyHolder.getKey().intValue();
        film.setId(filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET film_name = ?, description = ?, " +
                "release_date = ?, duration = ?, mpa_id = ? " +
                "where film_id = ?";
        int updatedRows = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (updatedRows == 0) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        String filmQuery = "SELECT * FROM film AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id ";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, rowNum) -> mapFilm(rs));
        return films;
    }

    @Override
    public Film getFilmById(Integer id) {
        String filmQuery = "SELECT * " +
                "FROM film AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE film_id = ?";
        Film film = null;
        try {
            film = jdbcTemplate.queryForObject(filmQuery, (rs, a) -> mapFilm(rs), id);
        } catch (DataAccessException e) {
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
        return film;
    }

    private Film mapFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        MPARating mpaRating = new MPARating();
        mpaRating.setId(rs.getInt("mpa_id"));
        mpaRating.setName(rs.getString("mpa_name"));
        film.setMpa(mpaRating);
        film.setLikes(getLikes(film.getId()));
        return film;
    }

    private Integer mapLikes(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }

    private Set<Integer> getLikes(Integer filmId) {
        String likesQuery = "SELECT * " +
                "FROM likes " +
                "WHERE film_id = ?";
        List<Integer> likes = jdbcTemplate.query(likesQuery, (rs, a) -> mapLikes(rs), filmId);
        return new HashSet<>(likes);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String insertQuery = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(insertQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String insertQuery = "delete from likes where film_id = ? AND user_id = ?";
        int updateRows = jdbcTemplate.update(insertQuery, filmId, userId);
        if (updateRows == 0) {
            throw new FilmNotFoundException("Фильм с id = " + filmId + "либо user_id =" + userId + " не найден.");
        }
    }

    @Override
    public Collection<Film> getPopularFilms(Integer limit) {
        String query = "SELECT f.*, m.MPA_NAME " +
                "FROM film AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.FILM_ID " +
                "JOIN mpa AS m ON f.MPA_ID = m.MPA_ID " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY count (l.user_ID)  DESC LIMIT ?";
        List<Film> topFilms = jdbcTemplate.query(query, (rs, i) -> mapFilm(rs), limit);
        return topFilms;
    }

}
