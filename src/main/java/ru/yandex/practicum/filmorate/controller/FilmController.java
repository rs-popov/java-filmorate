package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @GetMapping(value = {"/{id}"})
    public Film findFilm(@PathVariable Optional<Integer> id) {
        if (id.isPresent()) {
            return filmStorage.findFilm(id.get());
        } else {
            throw new InternalErrorException("");
        }
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        filmStorage.updateFilm(film);
        return film;
    }
}