package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alefilas.model.document.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {



}
