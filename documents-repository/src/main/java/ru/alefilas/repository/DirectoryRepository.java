package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    @Query("SELECT ae FROM AbstractEntity  ae WHERE Directory = ?1")
    List<AbstractEntity> findEntityByDirectory(Directory directory);

}
