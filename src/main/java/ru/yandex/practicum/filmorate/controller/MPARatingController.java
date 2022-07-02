package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InternalErrorException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MPARatingController {
    private final MPARatingService mpaRatingService;

    @Autowired
    public MPARatingController(MPARatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public Collection<MPARating> findAllMPARatings() {
        return mpaRatingService.getAllMPARatings();
    }

    @GetMapping(value = {"/{id}"})
    public MPARating findAllMPARatings(@PathVariable Integer id) {
        if (id != null) {
            return mpaRatingService.getMPARatingById(id);
        } else {
            throw new InternalErrorException("");
        }
    }
}