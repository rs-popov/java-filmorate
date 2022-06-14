package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void shouldAddFilm() {
        Film film = new Film("filmName",
                "filmDescription",
                LocalDate.of(2020, 10, 16),
                105);
        assertFalse(filmController.findAll().contains(film), "Фильм уже добавлен.");
        filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "Фильм не был добавлен.");
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film("filmName",
                "filmDescription",
                LocalDate.of(2020, 10, 16),
                105);
        filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "Фильм не добавлен.");
        film.setDuration(120);
        film.setDescription("NewDescription");
        filmController.put(film);
        assertTrue(filmController.findAll().contains(film), "Фильм не найден.");
        Film updFilm = (Film) filmController.findAll().toArray()[0];
        assertEquals(120, updFilm.getDuration(), "Продолжительность фильма не была обновлена.");
        assertEquals("NewDescription", updFilm.getDescription(), "Описание фильма не была обновлена.");
    }

    @Test
    void shouldNotCreateFilmWhenNullFilm() {
        Film film = null;
        assertEquals(0, filmController.findAll().size());
        try {
            filmController.create(film);
        } catch (Exception e) {
            assertEquals(0, filmController.findAll().size());
        }
    }

    @Test
    void shouldNotAddFilmWithEmptyName() {
        Film film = new Film("",
                "filmDescription",
                LocalDate.of(2020, 10, 16),
                105);
        assertTrue(film.getName().isEmpty(), "Описание фильма не пустое.");
        try {
            filmController.create(film);
        } catch (Exception e) {
            assertFalse(filmController.findAll().contains(film));
            assertEquals(e.getMessage(), "Пустое название фильма.", "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddFilmWithLongDescription() {
        Film film = new Film("filmName",
                "Loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooongDescription",
                LocalDate.of(2020, 10, 16),
                105);
        System.out.println(film.getDescription().length());
        assertTrue(film.getDescription().length() > 200, "Описание фильма не является неподходящим.");
        try {
            filmController.create(film);
        } catch (Exception e) {
            assertFalse(filmController.findAll().contains(film));
            assertEquals(e.getMessage(), "Слишком длинное описание фильма.", "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddFilmWithWrongData() {
        Film film = new Film("name",
                "filmDescription",
                LocalDate.of(1820, 10, 16),
                105);
        assertTrue(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)),
                "Дата релиза фильма не является недопустимой.");
        try {
            filmController.create(film);
        } catch (Exception e) {
            assertFalse(filmController.findAll().contains(film));
            assertEquals(e.getMessage(), "Неверная дата выхода фильма.", "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddFilmWithWrongDuration() {
        Film film = new Film("name",
                "filmDescription",
                LocalDate.of(2020, 10, 16),
                -105);
        assertTrue(film.getDuration() < 0, "Продолжительность фильма не является недопустимой.");
        try {
            filmController.create(film);
        } catch (Exception e) {
            assertFalse(filmController.findAll().contains(film));
            assertEquals(e.getMessage(), "Неверная продолжительность фильма.", "Неверная причина ошибки.");
        }
    }
}
