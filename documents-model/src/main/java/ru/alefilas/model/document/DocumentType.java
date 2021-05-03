package ru.alefilas.model.document;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "document_type")
@Data
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_type", nullable = false, unique = true)
    private String type;

}
