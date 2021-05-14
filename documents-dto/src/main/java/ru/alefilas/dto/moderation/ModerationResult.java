package ru.alefilas.dto.moderation;

import lombok.Data;
import ru.alefilas.model.moderation.ModerationStatus;

@Data
public class ModerationResult {

    private Long ticketId;
    private ModerationStatus result;
}
