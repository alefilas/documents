package ru.alefilas.service;

import org.springframework.data.domain.Page;
import ru.alefilas.dto.moderation.ModerationResult;
import ru.alefilas.dto.moderation.ModerationTicketDto;

public interface ModerationService {

    Page<ModerationTicketDto> getAllTickets(int page);

    void moderate(ModerationResult result);

}
