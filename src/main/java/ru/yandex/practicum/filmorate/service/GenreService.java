package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenresDAO;

import java.util.Collection;

@Component
public class GenreService {
    private final GenresDAO genresDAO;

    @Autowired
    public GenreService(GenresDAO genresDAO) {
        this.genresDAO = genresDAO;
    }

    public Collection<Genre> getAllGenres() {
        return genresDAO.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genresDAO.getGenreById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Genre not found."));
    }
}