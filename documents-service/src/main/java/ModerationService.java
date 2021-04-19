import model.directory.Directory;
import model.moderation.ModerationTicket;
import model.user.User;

import java.util.List;

public interface ModerationService {

    List<ModerationTicket> getAllModerationTickets();

    List<ModerationTicket> getAllModerationTicketsByDirectory(Directory directory, User user);

    ModerationTicket getModerationTicket(Directory directory, User user);
}
