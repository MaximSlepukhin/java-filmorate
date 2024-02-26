package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MPARatingDbStorage implements MPARatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPARatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPARating getMPARatingById(Integer id) {
        String queryMPARating = "SELECT * " +
                "FROM mpa " +
                "WHERE mpa_id = ?";
        MPARating mpaRating = new MPARating();

        SqlRowSet mpaRatingRow = jdbcTemplate.queryForRowSet(queryMPARating, id);
        if (mpaRatingRow.next()) {
            mpaRating.setId(mpaRatingRow.getInt("mpa_id"));
            mpaRating.setName(mpaRatingRow.getString("mpa_name"));
        } else {
            log.info("Жанр с идентификаторм" + id + " отсутствует в базе данных");
            throw new MPANotFoundException("Рейтинг с id = " + id + " не найден.");
        }
        return  mpaRating;
    }

    @Override
    public List<MPARating> getMPARatings() {
        String queryMPARating = "SELECT * FROM mpa";
        return jdbcTemplate.query(queryMPARating, this::mapRow);
    }

    public MPARating mapRow (ResultSet resultSet, int rowMow) throws SQLException {
        MPARating mpaRating = new MPARating();
        mpaRating.setId(resultSet.getInt("mpa_id"));
        mpaRating.setName(resultSet.getString("mpa_name"));
        return mpaRating;
    }
}