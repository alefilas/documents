package ru.alefilas.service.exception;

public class DocumentTypeNotFoundException extends NotFoundException {

    public DocumentTypeNotFoundException(String message) {
        super("Document type " + message + " not found");
    }
}
