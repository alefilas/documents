package ru.alefilas.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alefilas.model.document.DocumentType;

import java.util.Optional;

public interface DocumentTypeRepository extends CrudRepository<DocumentType, Long> {

    Optional<DocumentType> findByType(String type);
}
