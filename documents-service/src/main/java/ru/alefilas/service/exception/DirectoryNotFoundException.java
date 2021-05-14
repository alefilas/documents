package ru.alefilas.service.exception;

public class DirectoryNotFoundException extends NotFoundException {

    public DirectoryNotFoundException(Long id) {
        super("Directory with id=" + id + " not found");
    }
}
