package ru.alefilas.service.exception;

public class DocumentNotFoundException extends NotFoundException {

    public DocumentNotFoundException(Long id) {
        super("Document with id=" + id + " not found");
    }
}
