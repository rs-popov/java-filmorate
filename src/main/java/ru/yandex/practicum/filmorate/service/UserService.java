package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(int userId1, int userId2) {
        User user1 = userStorage.findUser(userId1);
        if (user1 == null) {
            log.warn("Пользователь с id " + userId1 + "не найден.");
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        User user2 = userStorage.findUser(userId2);
        if (user2 == null) {
            log.warn("Пользователь с id " + userId2 + "не найден.");
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        user1.addFriend(userId2);
        user2.addFriend(userId1);
        log.info("Пользователь с id={} и пользователь с id={} теперь друзья.", userId1, userId2);
    }

    public Collection<User> findAllFriends(int id) {
        return userStorage.findAllUsers().stream()
                .filter(user -> userStorage.findUser(id).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public void deleteFriends(int userId1, int userId2) {
        User user1 = userStorage.findUser(userId1);
        if (user1 == null) {
            log.warn("Пользователь с id " + userId1 + "не найден.");
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        User user2 = userStorage.findUser(userId2);
        if (user2 == null) {
            log.warn("Пользователь с id " + userId2 + "не найден.");
            throw new ObjectNotFoundException("Пользователь не найден.");
        }
        if (!user1.getFriends().contains(userId2) || !user2.getFriends().contains(userId1)) {
            log.warn("Пользователь с id={} и пользователь с id={} не являются друзьями.", userId1, userId2);
            throw new InternalErrorException("Неверные параменты запроса на удаление друзей.");
        } else {
            user1.deleteFriend(userId2);
            user2.deleteFriend(userId1);
            log.info("Пользователь с id={} и пользователь с id={} теперь не являются друзьями.", userId1, userId2);
        }
    }
}