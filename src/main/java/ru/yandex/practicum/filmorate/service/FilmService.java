package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        filmStorage.getFilmById(filmId).addLike(userId);
        log.info("Добавлен лайк фильму {}. ", film.getName());
    }

    public Collection<Film> findPopular(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new ObjectNotFoundException("Лайк от пользователя {id=" + userId + "} не найден.");
        } else {
            film.deleteLike(userId);
            log.info("Удален лайк фильму {}. ", film.getName());
        }
    }
}