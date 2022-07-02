package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPARatingDAO;

import java.util.Collection;

@Service
public class MPARatingService {
    private final MPARatingDAO mpaRatingDAO;

    @Autowired
    public MPARatingService(MPARatingDAO mpaRatingDAO) {
        this.mpaRatingDAO = mpaRatingDAO;
    }

    public Collection<MPARating> getAllMPARatings() {
        return mpaRatingDAO.getAllMPARating();
    }

    public MPARating getMPARatingById(int id) {
        return mpaRatingDAO.getMPARatingById(id)
                .orElseThrow(() -> new ObjectNotFoundException("MPA Rating not found."));
    }
}
