package ru.alefilas.impls;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.DocumentsDao;
import ru.alefilas.model.document.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DocumentsDaoJpa implements DocumentsDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Document save(Document document) {
        if (document.getId() == null) {
            entityManager.persist(document);
        } else {
            entityManager.merge(document);
        }
        return document;
    }

    @Override
    public Directory save(Directory directory) {
        if (directory.getId() == null) {
            entityManager.persist(directory);
        } else {
            entityManager.merge(directory);
        }
        return directory;
    }

    @Override
    public DocumentVersion save(DocumentVersion version, Long documentId) {
        if (version.getId() == null) {
            entityManager.persist(version);
        } else {
            entityManager.merge(version);
        }
        return version;
    }

    @Override
    public List<AbstractEntity> findEntityByDirectory(Directory directory) {

        List<AbstractEntity> list = new ArrayList<>();

        List<Document> documents = entityManager.createQuery("from Document ", Document.class).getResultList();
        List<Directory> directories = entityManager.createQuery("from Directory ", Directory.class).getResultList();

        list.addAll(documents);
        list.addAll(directories);

        return list;
    }

    @Override
    public void deleteById(Long id) {
        AbstractEntity entity = entityManager.find(AbstractEntity.class, id);
        entityManager.remove(entity);
    }

    @Override
    public Document findDocumentById(Long id) {
        return entityManager.find(Document.class, id);
    }

    @Override
    public DocumentVersion findVersionById(Long id) {
        return entityManager.find(DocumentVersion.class, id);
    }

    @Override
    public Directory findDirectoryById(Long id) {
        return entityManager.find(Directory.class, id);
    }

    @Override
    public List<DocumentVersion> findAllVersionByDocumentId(Long id) {
        return entityManager.createQuery("from DocumentVersion ", DocumentVersion.class).getResultList();
    }

    @Override
    public DocumentType findDocumentTypeByName(String name) {

        TypedQuery<DocumentType> query = entityManager.createQuery("from DocumentType where type = ?1", DocumentType.class);
        query.setParameter(1, name);

        return query.getSingleResult();
    }

    @Override
    public List<DocumentType> findAllDocumentTypes() {
        return entityManager.createQuery("from DocumentType ", DocumentType.class).getResultList();
    }
}
