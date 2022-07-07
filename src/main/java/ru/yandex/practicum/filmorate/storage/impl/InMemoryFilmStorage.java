package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

//@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static Integer globalId = 1;

    private static Integer getNextId() {
        return globalId++;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм с id " + id + " не найден.");
            throw new ObjectNotFoundException("Фильм не найден.");
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film addFilm(Film film) {
        if (validate(film)) {
            film.setId(getNextId());
            films.put(film.getId(), film);
        }
        log.info("Добавлен фильм {}. Общее число фильмов {}.", film.getName(), films.size());
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильм не найден.");
        } else films.remove(id);
        log.info("Удален фильм с id {}.", id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ObjectNotFoundException("Фильм не найден.");
        }
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