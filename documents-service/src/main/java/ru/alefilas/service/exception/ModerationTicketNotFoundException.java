package ru.alefilas.service.exception;

public class ModerationTicketNotFoundException extends NotFoundException{

    public ModerationTicketNotFoundException(Long id) {
        super("Ticket with id=" + id + " not found");
    }
}
