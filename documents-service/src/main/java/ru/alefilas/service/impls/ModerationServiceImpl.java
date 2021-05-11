package ru.alefilas.service.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.dto.moderation.ModerationResult;
import ru.alefilas.dto.moderation.ModerationTicketDto;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.moderation.ModerationTicket;
import ru.alefilas.repository.ModerationRepository;
import ru.alefilas.service.ModerationService;
import ru.alefilas.service.exception.ModerationTicketNotFoundException;
import ru.alefilas.service.mapper.ModerationMapper;

@Service
public class ModerationRepositoryImpl implements ModerationService {

    @Autowired
    private ModerationRepository moderationRepository;

    @Override
    @Transactional
    public Page<ModerationTicketDto> getAllTickets(int page) {
        return moderationRepository.findAll(PageRequest.of(page, 10))
                .map(ModerationMapper::modelToDto);
    }

    @Override
    @Transactional
    public ModerationTicketDto getFirstModerationTicket() {
        return ModerationMapper.modelToDto(moderationRepository.findFirst());
    }

    @Override
    @Transactional
    public void moderate(ModerationResult result) {

        ModerationTicket ticket = moderationRepository
                .findById(result.getTicketId())
                .orElseThrow(() -> new ModerationTicketNotFoundException(result.getTicketId()));

        if (result.getResult() != ModerationStatus.ON_MODERATION) {
            moderationRepository.deleteById(result.getTicketId());
        }

        Document document = ticket.getDocument();
        document.setStatus(result.getResult());
    }
}
