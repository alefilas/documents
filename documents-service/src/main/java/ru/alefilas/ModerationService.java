package ru.alefilas;

import ru.alefilas.model.document.Directory;
import ru.alefilas.model.moderation.ModerationTicket;
import ru.alefilas.model.user.User;

import java.util.List;

public interface ModerationService {

    List<ModerationTicket> getAllModerationTickets();

    List<ModerationTicket> getAllModerationTicketsByDirectory(Directory directory, User user);

    ModerationTicket getModerationTicket(Directory directory, User user);
}
