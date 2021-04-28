package ru.alefilas.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alefilas.DocumentsDao;
import ru.alefilas.EnumsDao;
import ru.alefilas.UsersDao;
import ru.alefilas.helper.DbConnector;
import ru.alefilas.model.document.*;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;

import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DocumentsDaoJdbc implements DocumentsDao {

    @Autowired
    private EnumsDao enumsDao;

    @Autowired
    private UsersDao usersDao;

    private static final String INSERT_ENTITY_IN_DIRECTORY = "INSERT INTO entity (creation_date, directory_id) VALUES (?, ?)";
    private static final String INSERT_ENTITY = "INSERT INTO entity (creation_date) VALUES (?)";
    private static final String INSERT_DOCUMENT = "INSERT INTO document (id, document_type_id, priority_id, user_id, status_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_DOCUMENT_VERSION = "INSERT INTO version (title, description, moderation_status_id, document_id) VALUES (?, ?, ?, ?)";
    private static final String INSERT_DIRECTORY = "INSERT INTO directory (id, title) VALUES (?, ?)";
    private static final String INSERT_FILES = "INSERT INTO file (path, version_id) VALUES (?, ?)";
    private static final String UPDATE_CURRENT_VERSION = "UPDATE document SET current_version_id = ?";
    private static final String SELECT_DOCUMENT = "SELECT * FROM entity e JOIN document d ON e.id = d.id WHERE e.id = ?";
    private static final String SELECT_DIRECTORY = "SELECT * FROM entity e JOIN directory d ON e.id = d.id WHERE e.id = ?";
    private static final String SELECT_DOCUMENT_VERSION = "SELECT * FROM version WHERE id = ?";
    private static final String SELECT_FILES = "SELECT path FROM file WHERE version_id = ?";
    private static final String SELECT_ALL_VERSIONS_BY_DOCUMENT = "SELECT * FROM version WHERE document_id = ?";
    private static final String SELECT_DOCUMENTS_BY_DIRECTORY = "SELECT * FROM entity e JOIN document d ON e.id = d.id WHERE directory_id = ? ORDER BY creation_date";
    private static final String SELECT_DIRECTORIES_BY_DIRECTORY = "SELECT * FROM entity e JOIN directory d ON e.id = d.id WHERE directory_id = ? ORDER BY creation_date";


    @Override
    public List<AbstractEntity> findEntityByDirectory(Directory directory) {

        ArrayList<AbstractEntity> list = new ArrayList<>();

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_DOCUMENTS_BY_DIRECTORY);
             PreparedStatement ps = connection.prepareStatement(SELECT_DIRECTORIES_BY_DIRECTORY)) {

            statement.setLong(1, directory.getId());
            ps.setLong(1, directory.getId());

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                list.add(findDocumentById(set.getLong("id")));
            }

            set = ps.executeQuery();
            while (set.next()) {
                list.add(findDirectoryById(set.getLong("id")));
            }


        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void deleteById(String table, Long id) {
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement("DELETE FROM " + table + " WHERE id = ?");
            ps.setLong(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Document findDocumentById(Long id) {
        Document document = null;
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SELECT_DOCUMENT);
            ps.setLong(1, id);

            ResultSet set = ps.executeQuery();
            if (set.next()) {

                document = new Document();

                findEntity(set, document);

                DocumentVersion version = findVersionById(set.getLong("current_version_id"));
                String type = enumsDao.findDocumentTypeById(set.getLong("document_type_id"));
                DocumentPriority priority = enumsDao.findPriorityById(set.getLong("priority_id"));
                User user = usersDao.findById(set.getLong("user_id"));
                ModerationStatus status = enumsDao.findModerationStatusById(set.getLong("status_id"));

                document.setId(id);
                document.setCurrentVersion(version);
                document.setVersions(findAllVersionByDocumentId(id));
                document.setType(type);
                document.setDocumentPriority(priority);
                document.setUser(user);
                document.setStatus(status);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return document;
    }

    @Override
    public DocumentVersion findVersionById(Long id) {

        DocumentVersion version = null;
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SELECT_DOCUMENT_VERSION);
            ps.setLong(1, id);

            ResultSet set = ps.executeQuery();
            if (set.next()) {

                ModerationStatus status = enumsDao.findModerationStatusById(set.getLong("moderation_status_id"));

                version = new DocumentVersion();
                version.setId(id);
                version.setTitle(set.getString("title"));
                version.setDescription(set.getString("description"));
                version.setStatus(status);
                version.setFiles(findFiles(id, connection));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return version;
    }

    @Override
    public Directory findDirectoryById(Long id) {

        if (id == 0) {
            return null;
        }

        Directory directory = null;
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SELECT_DIRECTORY);
            ps.setLong(1, id);

            ResultSet set = ps.executeQuery();
            if (set.next()) {

                directory = new Directory();

                findEntity(set, directory);

                String title = set.getString("title");

                directory.setTitle(title);
                directory.setId(id);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return directory;
    }

    @Override
    public List<DocumentVersion> findAllVersionByDocumentId(Long id) {

        List<DocumentVersion> versions = new ArrayList<>();
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_VERSIONS_BY_DOCUMENT);
            ps.setLong(1, id);

            ResultSet set = ps.executeQuery();
            while (set.next()) {

                DocumentVersion version = new DocumentVersion();

                ModerationStatus status = enumsDao.findModerationStatusById(set.getLong("moderation_status_id"));

                version.setId(set.getLong("id"));
                version.setTitle(set.getString("title"));
                version.setDescription(set.getString("description"));
                version.setStatus(status);
                version.setFiles(findFiles(set.getLong("id"), connection));

                versions.add(version);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return versions;
    }

    @Override
    public Document save(Document document) {

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_DOCUMENT);
             PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENT_VERSION)) {

            saveEntity(document, connection);

            ps.setLong(1, document.getId());
            ps.setLong(2, enumsDao.findIdOfDocumentType(document.getType()));
            ps.setLong(3, enumsDao.findIdOfPriority(document.getDocumentPriority()));
            ps.setLong(4, document.getUser().getId());
            ps.setLong(5, enumsDao.findIdOfModerationStatus(document.getStatus()));

            ps.executeUpdate();

            DocumentVersion version = save(document.getCurrentVersion(), document.getId());

            statement.setLong(1, version.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return document;
    }

    @Override
    public Directory save(Directory directory) {

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_DIRECTORY)) {

            saveEntity(directory, connection);

            ps.setLong(1, directory.getId());
            ps.setString(2, directory.getTitle());

            ps.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return directory;
    }

    @Override
    public DocumentVersion save(DocumentVersion version, Long documentId) {

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_DOCUMENT_VERSION, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, version.getTitle());
            statement.setString(2, version.getDescription());
            statement.setLong(3, enumsDao.findIdOfModerationStatus(version.getStatus()));
            statement.setLong(4, documentId);

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                version.setId(resultSet.getLong(1));
            }
            resultSet.close();

            saveFiles(version.getFiles(), version.getId(), connection);

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return version;
    }

    private List<Path> findFiles(Long id, Connection connection) throws SQLException {

        List<Path> files = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement(SELECT_FILES);
        ps.setLong(1, id);

        ResultSet set = ps.executeQuery();

        while (set.next()) {
            files.add(Path.of(set.getString("path")));
        }
        return files;
    }

    private void findEntity(ResultSet set, AbstractEntity entity) throws SQLException {
        LocalDate date = LocalDate.parse(set.getString("creation_date"));
        Directory parentDirectory = findDirectoryById(set.getLong("directory_id"));

        entity.setCreationDate(date);
        entity.setParentDirectory(parentDirectory);
    }

    private void saveFiles(List<Path> files, Long versionId, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(INSERT_FILES);

        for (Path path : files) {
            statement.setString(1, path.toString());
            statement.setLong(2, versionId);
            statement.executeUpdate();
        }
    }

    private void saveEntity(AbstractEntity entity, Connection connection) throws SQLException {

        PreparedStatement statement;

        LocalDate date = LocalDate.now();
        entity.setCreationDate(date);

        if (entity.getParentDirectory() != null) {
            statement = connection.prepareStatement(INSERT_ENTITY_IN_DIRECTORY, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(date));
            statement.setLong(2, entity.getParentDirectory().getId());
        } else {
            statement = connection.prepareStatement(INSERT_ENTITY, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(date));
        }

        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong(1));
        }
    }
}
