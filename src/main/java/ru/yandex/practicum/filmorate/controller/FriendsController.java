package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{id}/friends")
@Slf4j
public class FriendsController {
    private final UserService userService;

    @Autowired
    public FriendsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllFriends(@PathVariable int id) {
        return userService.findAllFriends(id);
    }

    @PutMapping("/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriends(id, friendId);
    }

    @GetMapping("/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriends(id, friendId);
    }
}