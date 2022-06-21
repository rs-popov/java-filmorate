package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @NonNull
    @Email
    private String email;

    @NonNull
    @Pattern(regexp = "^\\S*$")
    private String login;

    @NonNull
    private String name;

    @NonNull
    @PastOrPresent
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(int friendId) {
        friends.remove(friendId);
    }
}