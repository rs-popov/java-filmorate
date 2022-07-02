package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class MPARatingDAOImpl implements MPARatingDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPARatingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPARating> getAllMPARating() {
        String sqlQuery = "select * from MPA_RATING";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPARating);
    }

    @Override
    public Optional<MPARating> getMPARatingById(int id) {
        String sqlQuery = "select * from MPA_RATING where MPA_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPARating, id));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MPARating> getMPARatingByFilmId(int id) {
        String sqlQuery = "select MPA_ID, MPA_NAME from FILMS AS f " +
                "LEFT OUTER JOIN MPA_RATING AS mpa ON f.FILM_MPA_ID = mpa.MPA_ID where FILM_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMPARating, id));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private MPARating mapRowToMPARating(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new ObjectNotFoundException("Рейтинг не найден");
        }
        return MPARating.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}