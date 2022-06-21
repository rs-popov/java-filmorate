package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static Integer globalId = 1;

    private static Integer getNextId() {
        return globalId++;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id={}  не найден.", id);
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        if (validate(user)) {
            user.setId(getNextId());
            users.put(user.getId(), user);
        }
        log.info("Добавлен новый пользователь {}. Общее число пользователей {}",
                user.getLogin(), users.size());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        if (validate(user)) {
            users.put(user.getId(), user);
        }
        log.info("Изменен профиль пользователя {}.", user.getLogin());
        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден.");
        } else users.remove(id);
        log.info("Удален профиль пользователя с id {}.", id);
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