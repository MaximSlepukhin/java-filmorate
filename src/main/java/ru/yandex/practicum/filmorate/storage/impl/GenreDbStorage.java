package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String queryGenre = "SELECT * " +
                "FROM genre " +
                "WHERE genre_id = ?";
        Genre genre = new Genre();

        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(queryGenre, id);
        if (genreRow.next()) {
            genre.setId(genreRow.getInt("genre_id"));
            genre.setName(genreRow.getString("genre_name"));
        } else {
            log.info("Жанр с идентификаторм {} " + id + "отсутствует в базе данных");
            throw new GenreNotFoundException("Жанр с идентификаторм {} " + id + "отсутствует в базе данных");
        }
        return genre;
    }

    @Override
    public List<Genre> getGenres() {
        String queryGenre = "SELECT * FROM genre ORDER BY genre_id ASC";
        return jdbcTemplate.query(queryGenre, this::mapRow);
    }

    public Genre mapRow(ResultSet resultSet, int rowNow) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("genre_name"));
        return genre;
    }

    @Override
    public Set<Genre> addGenresForFilm(Set<Genre> genresFromFilm, Integer id) {
        ArrayList<Genre> genreList = new ArrayList<>();
        try {
            genreList.addAll(genresFromFilm);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String querySQL = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(querySQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setInt(2, genreList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
        return new TreeSet<>(genresFromFilm);
    }

    @Override
    public Set<Genre> updateGenresForFilm(Set<Genre> genresFromFilm, Integer filmId) {
        String deleteSql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, filmId);
        return new TreeSet<>(addGenresForFilm(genresFromFilm, filmId));
    }

    @Override
    public Set<Genre> setGenresForFilmList(Integer filmId) {
        String genresQuery = "SELECT * " +
                "FROM film_genre AS fg " +
                "JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(genresQuery, (rs, a) -> mapGenres(rs), filmId);
        return new TreeSet<>(genres);
    }

    public Genre mapGenres(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    }
}
