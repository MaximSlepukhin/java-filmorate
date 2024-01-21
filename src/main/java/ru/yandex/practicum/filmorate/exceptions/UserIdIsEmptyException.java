package ru.yandex.practicum.filmorate.exceptions;

public class UserIdIsEmptyException extends RuntimeException{
    public UserIdIsEmptyException(String message) {
        super(message);
    }
}
