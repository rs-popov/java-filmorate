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

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден."));
    }

    public User createUser(User user) {
        userStorage.createUser(user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    public void addFriends(int userId1, int userId2) {
        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);
        user1.addFriend(user2.getId());
        userStorage.updateUser(user1);
        log.info("Пользователь с id={} и пользователь с id={} теперь друзья.", userId1, userId2);
    }

    public Collection<User> findAllFriends(int id) {
        return userStorage.getAllUsers().stream()
                .filter(user -> getUserById(id).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public Collection<User> findCommonFriends(int id, int otherId) {
        return findAllFriends(id).stream()
                .filter(user -> findAllFriends(otherId).contains(user))
                .collect(Collectors.toList());
    }

    public void deleteFriends(int userId1, int userId2) {
        User user1 = getUserById(userId1);
        if (!user1.getFriends().contains(userId2) ) {
            log.warn("Пользователь с id={} и пользователь с id={} не являются друзьями.", userId1, userId2);
            throw new InternalErrorException("Неверные параменты запроса на удаление друзей.");
        } else {
            user1.deleteFriend(userId2);
            userStorage.updateUser(user1);
            log.info("Пользователь с id={} и пользователь с id={} теперь не являются друзьями.", userId1, userId2);
        }
    }
}