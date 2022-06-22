package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    private int id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @Size(min = 1, max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private int duration;

    private final Set<Integer> likes = new HashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(int userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film film) {
        return film.getLikes().size() - this.getLikes().size();
    }
}