package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping(value = {"/{id}"})
    public Film findFilm(@PathVariable Integer id) {
        if (id != null) {
            return filmService.getFilmById(id);
        } else {
            throw new InternalErrorException("");
        }
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        filmService.updateFilm(film);
        return film;
    }
}