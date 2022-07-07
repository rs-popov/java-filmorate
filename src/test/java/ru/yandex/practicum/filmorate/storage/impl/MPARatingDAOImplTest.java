package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.MPARatingDAO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPARatingDAOImplTest {
    @Autowired
    private MPARatingDAO mpaRatingDAO;

    @Test
    void getAllMPARating() {
        assertEquals(5, mpaRatingDAO.getAllMPARating().size());
    }

    @Test
    void getMPARatingById() {
        assertEquals("G", mpaRatingDAO.getMPARatingById(1).get().getName());
    }
}