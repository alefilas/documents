package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.moderation.ModerationTicket;

import java.util.Optional;

@Repository
public interface ModerationRepository extends JpaRepository<ModerationTicket, Long> {

    Optional<ModerationTicket> findFirstByDocument(Document document);

}
