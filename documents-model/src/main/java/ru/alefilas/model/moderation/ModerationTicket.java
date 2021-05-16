package ru.alefilas.model.moderation;

import lombok.Data;
import ru.alefilas.model.document.Document;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "moderation_ticket")
public class ModerationTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

}
