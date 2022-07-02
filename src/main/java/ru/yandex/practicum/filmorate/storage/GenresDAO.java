package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

public interface GenresDAO {

    Collection<Genre> getAllGenres();

    Optional<Genre> getGenreById(int id);

    HashSet<Genre> getGenresByFilmId(int id);
}
