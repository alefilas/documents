package ru.alefilas.service.exception;

import ru.alefilas.model.permit.PermitType;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(Long id, PermitType type) {
        super("No " + type + " access to directory with id=" + id);
    }
}
