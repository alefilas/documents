package ru.alefilas.model.moderation;

import lombok.Data;
import ru.alefilas.model.document.Document;

import java.time.LocalDate;

@Data
public class ModerationTicket {

    private Long id;
    private Document document;
    private LocalDate creationDate;
}
