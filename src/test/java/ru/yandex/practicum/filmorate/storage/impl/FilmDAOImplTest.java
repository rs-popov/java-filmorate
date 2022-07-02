package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDAOImplTest {

    @Autowired
    private FilmStorage filmStorage;

    @Test
    void getAllFilms() {
        assertFalse(filmStorage.getAllFilms().isEmpty());
    }

    @Test
    void addFilm() {
        Film film = Film.builder()
                .id(1)
                .name("film name")
                .description("film description")
                .mpa(MPARating.builder()
                        .id(1).name("G").build())
                .duration(120)
                .releaseDate(LocalDate.of(2022, Month.APRIL,1))
                .genres(null)
                .likes(null)
                .build();
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getAllFilms().size());
    }

    @Test
    void getFilmById() {
        assertTrue(filmStorage.getFilmById(1).isPresent());
    }

    @Test
    void updateFilm() {
        Film filmUpd = Film.builder()
                .id(1)
                .name("film nameUPD")
                .description("film descriptionUPD")
                .mpa(MPARating.builder()
                        .id(1).name("G").build())
                .duration(120)
                .releaseDate(LocalDate.of(2022, Month.APRIL,1))
                .genres(null)
                .likes(null)
                .build();
        filmStorage.updateFilm(filmUpd);

        assertEquals("film nameUPD", filmStorage.getFilmById(1).get().getName());
    }

    @Test
    void deleteFilm() {
        filmStorage.deleteFilm(1);
        assertEquals(0, filmStorage.getAllFilms().size());
    }
}