package ru.alefilas.model.moderation;

import lombok.Data;
import ru.alefilas.model.document.Document;

@Data
public class ModerationTicket {

    private Long id;
    private Document document;
}
