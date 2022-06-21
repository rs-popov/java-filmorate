package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void shouldAddNewUser() {
        User user = new User("user@mail.ru",
                "userLogin",
                "UserName",
                LocalDate.of(2000, 1, 1));
        assertFalse(userController.findAllUsers().contains(user), "Пользователь уже не был добавлен.");
        userController.create(user);
        assertTrue(userController.findAllUsers().contains(user), "Пользователь не был добавлен.");
    }

    @Test
    void shouldUpdateUser() {
        User user = new User("user@mail.ru",
                "userLogin",
                "UserName",
                LocalDate.of(2000, 1, 1));
        userController.create(user);
        assertTrue(userController.findAllUsers().contains(user), "Пользователь не был добавлен.");
        user.setName("updUserName");
        user.setEmail("updUser@mail.ru");
        userController.put(user);
        assertTrue(userController.findAllUsers().contains(user), "Пользователь не был добавлен.");
        User updUser = (User) userController.findAllUsers().toArray()[0];
        assertEquals("updUserName", updUser.getName(), "Имя пользователя не было обновлено.");
        assertEquals("updUser@mail.ru", updUser.getEmail(), "Почта пользователя не была обновлена.");
    }

    @Test
    void shouldNotAddUserWhenNullUser() {
        User user = null;
        assertEquals(0, userController.findAllUsers().size());
        try {
            userController.create(user);
        } catch (Exception e) {
            assertEquals(0, userController.findAllUsers().size());
        }
    }

    @Test
    void shouldNotAddUserWhenEmptyEmail() {
        User user = new User("",
                "userLogin",
                "UserName",
                LocalDate.of(2000, 1, 1));
        assertTrue(user.getEmail().isEmpty(), "Почта не является недопустимой.");
        try {
            userController.create(user);
        } catch (Exception e) {
            assertFalse(userController.findAllUsers().contains(user), "Пользователь был добавлен.");
            assertEquals("Неверный формат электронной почты.", e.getMessage(), "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddUserWhenWrongEmail() {
        User user = new User("usermail.ru",
                "userLogin",
                "UserName",
                LocalDate.of(2000, 1, 1));
        assertFalse(user.getEmail().contains("@"), "Почта не является недопустимой.");
        try {
            userController.create(user);
        } catch (Exception e) {
            assertFalse(userController.findAllUsers().contains(user), "Пользователь был добавлен.");
            assertEquals("Неверный формат электронной почты.", e.getMessage(), "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddUserWhenEmptyLogin() {
        User user = new User("user@mail.ru",
                "",
                "UserName",
                LocalDate.of(2000, 1, 1));
        assertTrue(user.getLogin().isEmpty(), "Логин не является недопустимым.");
        try {
            userController.create(user);
        } catch (Exception e) {
            assertFalse(userController.findAllUsers().contains(user), "Пользователь был добавлен.");
            assertEquals("Неверный формат логина.", e.getMessage(), "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldNotAddUserWhenWrongLogin() {
        User user = new User("user@mail.ru",
                "user Login",
                "UserName",
                LocalDate.of(2000, 1, 1));
        assertFalse(user.getEmail().contains(" "), "Логин не является недопустимым.");
        try {
            userController.create(user);
        } catch (Exception e) {
            assertFalse(userController.findAllUsers().contains(user), "Пользователь был добавлен.");
            assertEquals("Неверный формат логина.", e.getMessage(), "Неверная причина ошибки.");
        }
    }

    @Test
    void shouldAddUserWhenEmptyName() {
        User user = new User("user@mail.ru",
                "userLogin",
                "",
                LocalDate.of(2000, 1, 1));
        final String nameOld = user.getName();
        assertTrue(user.getName().isEmpty(), "Имя не является недопустимым.");
        userController.create(user);
        assertTrue(userController.findAllUsers().contains(user), "Пользователь не был добавлен.");
        User updUser = (User) userController.findAllUsers().toArray()[0];
        assertNotEquals(nameOld, updUser.getName(), "Имя пользователя не было обновлено.");
        assertEquals(updUser.getLogin(), updUser.getName(),
                "Имя пользователя после изменения не соответствует логину.");
    }

    @Test
    void shouldNotAddUserWhenBirthdayInFuture() {
        User user = new User("user@mail.ru",
                "userLogin",
                "userName",
                LocalDate.of(2023, 1, 1));
        LocalDate now = LocalDate.now();
        assertTrue(user.getBirthday().isAfter(now), "День рождения пользлвателя не является недопустимым.");
        try {
            userController.create(user);
        } catch (Exception e) {
            assertFalse(userController.findAllUsers().contains(user), "Пользователь был добавлен.");
            assertEquals("Неверная дата рождения.", e.getMessage(), "Неверная причина ошибки.");
        }
    }
}