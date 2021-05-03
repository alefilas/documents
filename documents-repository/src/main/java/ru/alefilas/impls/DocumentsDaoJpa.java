package ru.alefilas.impls;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.DocumentsDao;
import ru.alefilas.model.document.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentsDaoJpa implements DocumentsDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public Document save(Document document) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(document);
        return document;
    }

    @Override
    @Transactional
    public Directory save(Directory directory) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(directory);
        return directory;
    }

    @Override
    @Transactional
    public DocumentVersion save(DocumentVersion version, Long documentId) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(version);
        return version;
    }

    @Override
    @Transactional
    public List<AbstractEntity> findEntityByDirectory(Directory directory) {

        List<AbstractEntity> list = new ArrayList<>();

        Session session = sessionFactory.getCurrentSession();

        List<Document> documents = session.createQuery("from Document ", Document.class).getResultList();
        List<Directory> directories = session.createQuery("from Directory ", Directory.class).getResultList();

        list.addAll(documents);
        list.addAll(directories);

        return list;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        AbstractEntity entity = sessionFactory.getCurrentSession().load(AbstractEntity.class, id);
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    @Transactional
    public Document findDocumentById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Document.class, id);
    }

    @Override
    @Transactional
    public DocumentVersion findVersionById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(DocumentVersion.class, id);
    }

    @Override
    @Transactional
    public Directory findDirectoryById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Directory.class, id);
    }

    @Override
    @Transactional
    public List<DocumentVersion> findAllVersionByDocumentId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from DocumentVersion ", DocumentVersion.class).getResultList();
    }

    @Override
    @Transactional
    public DocumentType findDocumentTypeByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        Query<DocumentType> query = session.createQuery("from DocumentType where type = ?1", DocumentType.class);
        query.setParameter(1, name);

        return query.getSingleResult();
    }

    @Override
    @Transactional
    public List<DocumentType> findAllDocumentTypes() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from DocumentType ", DocumentType.class).getResultList();
    }
}
