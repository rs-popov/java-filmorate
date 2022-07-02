package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDAOImplTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    void createUser() {
        User user = User.builder()
                .id(1)
                .name("User name")
                .email("user@mail.ru")
                .birthday(LocalDate.of(1990, Month.APRIL, 1))
                .login("Userlogin")
                .build();
        userStorage.createUser(user);
        assertEquals(1, userStorage.getAllUsers().size());
    }

    @Test
    void getAllUsers() {
        assertTrue(userStorage.getAllUsers().isEmpty());
    }

    @Test
    void deleteUser() {
        userStorage.deleteUser(1);
        assertEquals(0, userStorage.getAllUsers().size());
    }
}