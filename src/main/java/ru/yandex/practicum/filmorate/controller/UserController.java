package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (validate(user)) {
            users.put(user.getId(), user);
        }
        log.info("Добавлен новый пользователь {}. Общее число пользователей {}",
                user.getLogin(), users.size());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (validate(user)) {
            users.put(user.getId(), user);
        }
        log.info("Изменен профиль пользователя {}.", user.getLogin());
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
