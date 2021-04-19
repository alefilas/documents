
import model.moderation.ModerationTicket;

import java.util.List;

public interface ModerationRepository {

    List<ModerationTicket> findAllByDirectory();

    ModerationTicket findFirstByDirectory();

}
