package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Qualifier
@Component
@Slf4j
public class FilmDAOImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenresDAOImpl genresDAO;

    @Autowired
    public FilmDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        genresDAO = new GenresDAOImpl(jdbcTemplate);
    }

    @Override
    public Film addFilm(Film film) {
        if (getFilmById(film.getId()).isPresent()) {
            log.warn("Фильм с id={} уже добавлен", film.getId());
            throw new UserAlreadyExistException("Фильм с id=" + film.getId() + "уже добавлен");
        }
        if (validate(film)) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("FILMS").usingGeneratedKeyColumns("FILM_ID");

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("FILM_NAME", film.getName())
                    .addValue("FILM_DESCRIPTION", film.getDescription())
                    .addValue("FILM_RELEASEDATE", film.getReleaseDate())
                    .addValue("FILM_MPA_ID", film.getMpa().getId())
                    .addValue("FILM_DURATION", film.getDuration());

            Number id = jdbcInsert.executeAndReturnKey(parameters);

            film.setId(id.intValue());
            addGenresByFilm(film);
            log.info("Добавлен новый фильм id={}, name={}", film.getId(), film.getName());
        }
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        if (getFilmById(id).isEmpty()) {
            log.warn("Фильма с id={} не существует.", id);
            throw new ObjectNotFoundException("Фильма с id=" + id + "не существует.");
        }
        jdbcTemplate.update("delete from FILMS where FILM_ID = ?", id);
        log.info("Удален фильм с id {}.", id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()).isEmpty()) {
            log.warn("Фильма с id={} не существует.", film.getId());
            throw new ObjectNotFoundException("Фильма с id=" + film.getId() + "не существует.");
        }
        if (validate(film)) {
            String sqlQuery = "update FILMS set " +
                    "FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASEDATE = ?, FILM_DURATION = ?, " +
                    "FILM_MPA_ID = ? where FILM_ID = ?";
            jdbcTemplate.update(sqlQuery, film.getName(),
                    film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getMpa().getId(), film.getId());
        }
        addGenresByFilm(film);
        addLikesByFilm(film);
        log.info("Изменен фильм {}.", film.getName());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "select * from FILMS JOIN MPA_RATING MR on MR.MPA_ID = FILMS.FILM_MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sqlQuery = "select * from FILMS JOIN MPA_RATING MR on MR.MPA_ID = FILMS.FILM_MPA_ID where FILM_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new ObjectNotFoundException("Фильм не найден");
        }
        Film film = Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("FILM_DESCRIPTION"))
                .releaseDate(resultSet.getDate("FILM_RELEASEDATE").toLocalDate())
                .duration(resultSet.getInt("FILM_DURATION"))
                .mpa(MPARating.builder()
                        .id(resultSet.getInt("FILM_MPA_ID"))
                        .name(resultSet.getString("MPA_NAME"))
                        .build())
                .likes(null)
                .genres(null)
                .build();

        film.setGenres(genresDAO.getGenresByFilmId(film.getId()));

        String sqlQuery = "select USER_ID from LIKES where FILM_ID=?";
        HashSet<Integer> likes = new HashSet<>(jdbcTemplate.query(sqlQuery,
                (rs, num) -> rs.getInt("user_id"), film.getId()));
        film.setLikes(likes);
        validate(film);
        return film;
    }

    private boolean validate(Film film) {
        if (film.getName().isEmpty()) {
            log.warn("Пустое название фильма. Фильм не был добавлен.");
            throw new ValidationException("Пустое название фильма.");
        }
        if (film.getDescription().isEmpty()) {
            log.warn("Пустое описание фильма. Фильм не был добавлен.");
            throw new ValidationException("Пустое описание фильма.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Слишком длинное описание фильма. Фильм не был добавлен.");
            throw new ValidationException("Слишком длинное описание фильма.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Неверная дата выхода фильма. Фильм не был добавлен.");
            throw new ValidationException("Неверная дата выхода фильма.");
        }
        if (film.getDuration() < 0) {
            log.warn("Неверная продолжительность фильма. Фильм не был добавлен.");
            throw new ValidationException("Неверная продолжительность фильма.");
        }
        return true;
    }

    private void addGenresByFilm(Film film) {
        jdbcTemplate.update("delete from FILM_GENRES where FILM_ID=?", film.getId());
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update("insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?,?)",
                        film.getId(), g.getId());
            }
        }
    }

    private void addLikesByFilm(Film film) {
        jdbcTemplate.update("delete from LIKES where FILM_ID=?", film.getId());
        if (film.getLikes() != null) {
            for (int likeId:film.getLikes()) {
                jdbcTemplate.update("insert into LIKES (FILM_ID, USER_ID) values (?,?)", film.getId(), likeId);
            }
        }
    }
}