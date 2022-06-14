package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (validate(film)) {
            films.put(film.getId(), film);
        }
        log.info("Добавлен фильм {}. Общее число фильмов {}.", film.getName(), films.size());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (validate(film)) {
            films.put(film.getId(), film);
        }
        log.info("Изменен фильм {}.", film.getName());
        return film;
    }

    private boolean validate(Film film) {
        if (film.getName().isEmpty()) {
            log.warn("Пустое название фильма. Фильм не был добавлен.");
            throw new ValidationException("Пустое название фильма.");
        }
        if (film.getDescription().isEmpty()) {
            log.warn("Пустое описание фильма. Фильм не был добавлен.");
            throw new ValidationException("Пустое описание фильма.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Слишком длинное описание фильма. Фильм не был добавлен.");
            throw new ValidationException("Слишком длинное описание фильма.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Неверная дата выхода фильма. Фильм не был добавлен.");
            throw new ValidationException("Неверная дата выхода фильма.");
        }
        if (film.getDuration() < 0) {
            log.warn("Неверная продолжительность фильма. Фильм не был добавлен.");
            throw new ValidationException("Неверная продолжительность фильма.");
        }
        return true;
    }
}
