package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = {"/{id}"})
    public User findAllUsers(@PathVariable Integer id) {
        if (id != null) {
            return userService.getUserById(id);
        } else {
            throw new InternalErrorException("");
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userService.createUser(user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        userService.updateUser(user);
        return user;
    }
}