package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Primary
@Component
public class UserDAOImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        if (getUserById(user.getId()).isPresent()) {
            log.warn("Пользователь с id={} уже добавлен", user.getId());
            throw new UserAlreadyExistException("Пользователь с id=" + user.getId() + "уже добавлен");
        }
        if (validate(user)) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("USERS").usingGeneratedKeyColumns("USER_ID");
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("USER_EMAIL", user.getEmail())
                    .addValue("USER_LOGIN", user.getLogin())
                    .addValue("USER_NAME", user.getName())
                    .addValue("USER_BIRTHDAY", user.getBirthday());
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            user.setId(id.intValue());
            log.info("Пользователь с id={} добавлен.", user.getId());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserById(user.getId()).isEmpty()) {
            log.warn("Пользователя с id={} не существует.", user.getId());
            throw new ObjectNotFoundException("Пользователя с id=" + user.getId() + "не существует.");
        }
        String sqlQuery = "update USERS set " +
                "USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, USER_BIRTHDAY = ?" +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        jdbcTemplate.update("delete from FRIENDSHIP where USER_ID=?", user.getId());
        if (user.getFriends() != null) {
            user.getFriends().forEach(id -> jdbcTemplate.update("insert into FRIENDSHIP (USER_ID, FRIEND_ID) " +
                    "values (?,?)", user.getId(), id));
        }

        log.info("Изменен профиль пользователя id={}, login={}", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (getUserById(id).isEmpty()) {
            log.warn("Пользователя с id={} не существует.", id);
            throw new ObjectNotFoundException("Пользователя с id=" + id + "не существует.");
        }
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Удален профиль пользователя с id {}.", id);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> getUserById(int id) {
        String sqlQuery = "select * from USERS where USER_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new ObjectNotFoundException("Пользователь не найден.");
        }

        User user = User.builder()
                .id(resultSet.getInt("USER_ID"))
                .email(resultSet.getString("USER_EMAIL"))
                .login(resultSet.getString("USER_LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("USER_BIRTHDAY").toLocalDate())
                .build();

        String sqlQuery = "select FRIEND_ID from FRIENDSHIP where USER_ID=?";
        HashSet<Integer> friends = new HashSet<>(jdbcTemplate.query(sqlQuery,
                (rs, num) -> rs.getInt("FRIEND_ID"), resultSet.getInt("USER_ID")));
        friends.forEach(user::addFriend);
        return user;
    }

    private boolean validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Неверный формат электронной почты. Пользователь не был добавлен.");
            throw new ValidationException("Неверный формат электронной почты.");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Неверный формат логина. Пользователь не был добавлен.");
            throw new ValidationException("Неверный формат логина.");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.warn("Пустое имя пользователя. В качестве имени пользователя был использован логин.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неверная дата рождения - дата рождения не может быть в будущем. Пользователь не был добавлен.");
            throw new ValidationException("Неверная дата рождения.");
        }
        return true;
    }
}