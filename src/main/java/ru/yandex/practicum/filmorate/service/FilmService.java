package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        if (film == null) {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        filmStorage.findFilm(filmId).addLike(userId);
        log.info("Добавлен лайк фильму {}. ", film.getName());
    }

    public Collection<Film> findPopular(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted((f0, f1) -> f1.getLikes().size()-f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        if (film == null) {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        if (!film.getLikes().contains(userId)) {
            throw new ObjectNotFoundException("Лайк от пользователя {id="+userId+"} не найден.");
        } else {
            film.deleteLike(userId);
            log.info("Удален лайк фильму {}. ", film.getName());
        }
    }
}
