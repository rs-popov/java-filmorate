package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class GenresDAOImpl implements GenresDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    @Override
    public HashSet<Genre> getGenresByFilmId(int id) {
        String sqlQuery = "select g.GENRE_ID, g.GENRE_NAME from GENRES AS g " +
                "LEFT OUTER JOIN FILM_GENRES AS fg ON g.GENRE_ID=fg.GENRE_ID " +
                " where fg.FILM_ID = ?";
        HashSet<Genre> genres = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
        if (genres.isEmpty()) {
            return null;
        } else {
            return genres;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new ObjectNotFoundException("Жанр не найден");
        }
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}