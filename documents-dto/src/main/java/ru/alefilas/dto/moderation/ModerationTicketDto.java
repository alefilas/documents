package ru.alefilas.dto.moderation;

import lombok.Data;
import ru.alefilas.dto.documents.OutputDocumentDto;

@Data
public class ModerationTicketDto {

    private Long id;
    private OutputDocumentDto document;

}
