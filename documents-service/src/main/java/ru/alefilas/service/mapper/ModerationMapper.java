package ru.alefilas.service.mapper;

import ru.alefilas.dto.moderation.ModerationTicketDto;
import ru.alefilas.model.moderation.ModerationTicket;

public class ModerationMapper {

    public static ModerationTicketDto modelToDto(ModerationTicket ticket) {

        ModerationTicketDto dto = new ModerationTicketDto();
        dto.setId(ticket.getId());
        dto.setDocument(DocumentMapper.modelToDto(ticket.getDocument()));
        return dto;
    }
}
