package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping(value = {"/{id}"})
    public Genre findAllGenres(@PathVariable Integer id) {
        if (id != null) {
            return genreService.getGenreById(id);
        } else {
            throw new InternalErrorException("Произошла ошибка. Неверный идентификатор жанра.");
        }
    }
}