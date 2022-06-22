package ru.yandex.practicum.filmorate.exceptions;

public class InternalErrorException extends RuntimeException{
    public InternalErrorException(String s) {
        super(s);
    }
}