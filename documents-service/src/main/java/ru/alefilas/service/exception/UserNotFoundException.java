package ru.alefilas.service.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super("User with username=" + message + " not found");
    }
}
