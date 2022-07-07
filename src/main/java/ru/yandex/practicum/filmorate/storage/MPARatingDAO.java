package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;
import java.util.Optional;

public interface MPARatingDAO {

    Collection<MPARating> getAllMPARating();

    Optional<MPARating> getMPARatingById(int id);

    Optional<MPARating> getMPARatingByFilmId(int id);
}