package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.moderation.ModerationResult;
import ru.alefilas.dto.moderation.ModerationTicketDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.moderation.ModerationTicket;
import ru.alefilas.model.permit.PermitType;
import ru.alefilas.repository.ModerationRepository;
import ru.alefilas.service.ModerationService;
import ru.alefilas.service.access.AccessHelper;
import ru.alefilas.service.exception.ModerationTicketNotFoundException;
import ru.alefilas.service.mapper.ModerationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModerationServiceImpl implements ModerationService {

    @Autowired
    private ModerationRepository moderationRepository;

    @Override
    @Transactional
    public Page<ModerationTicketDto> getAllTickets(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        List<ModerationTicket> visibleTickets = moderationRepository.findAll()
                .stream()
                .filter(tic -> AccessHelper.checkAccessBoolean(tic.getDocument().getParentDirectory(), PermitType.MODERATE))
                .collect(Collectors.toList());

        Page<ModerationTicket> tickets = new PageImpl<>(visibleTickets, pageable, visibleTickets.size());

        return tickets.map(ModerationMapper::modelToDto);
    }

    @Override
    @Transactional
    public void moderate(ModerationResult result) {

        ModerationTicket ticket = moderationRepository
                .findById(result.getTicketId())
                .orElseThrow(() -> new ModerationTicketNotFoundException(result.getTicketId()));

        AccessHelper.checkAccess(ticket.getDocument().getParentDirectory(), PermitType.MODERATE);

        if (result.getResult() != ModerationStatus.ON_MODERATION) {
            moderationRepository.deleteById(result.getTicketId());
        }

        Document document = ticket.getDocument();
        document.setStatus(result.getResult());
    }
}
