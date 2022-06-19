package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @GetMapping(value = {"/{id}"})
    public User findAllUsers(@PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            return userStorage.findUser(id.get());
        } else {
            throw new InternalErrorException("");
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userStorage.createUser(user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        userStorage.updateUser(user);
        return user;
    }
}