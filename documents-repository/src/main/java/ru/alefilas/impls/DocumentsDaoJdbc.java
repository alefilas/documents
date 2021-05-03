package ru.alefilas.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.alefilas.DocumentsDao;
import ru.alefilas.UsersDao;
import ru.alefilas.helper.DbConnector;
import ru.alefilas.model.document.*;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DocumentsDaoJdbc implements DocumentsDao {

    @Autowired
    @Qualifier("usersDaoJdbc")
    private UsersDao usersDao;

    private static final String INSERT_ENTITY_IN_DIRECTORY = "INSERT INTO entity (creation_date, directory_id) VALUES (?, ?)";
    private static final String INSERT_ENTITY = "INSERT INTO entity (creation_date) VALUES (?)";
    private static final String INSERT_DOCUMENT = "INSERT INTO document (id, document_type_id, priority, status, user_id) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_DOCUMENT_VERSION = "INSERT INTO version (title, description, status, document_id) VALUES (?, ?, ?, ?)";
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
    private static final String SELECT_TYPE_BY_ID = "SELECT * FROM document_type WHERE id = ?";
    private static final String SELECT_TYPE_BY_NAME = "SELECT * FROM document_type WHERE document_type = ?";
    private static final String SELECT_ALL_TYPES = "SELECT * FROM document_type";


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
    public void deleteById(Long id) {
        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement("DELETE FROM entity WHERE id = ?");
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
                DocumentType type = findDocumentTypeById(set.getLong("document_type_id"), connection);
                DocumentPriority priority = DocumentPriority.valueOf(set.getString("priority"));
                User user = usersDao.findById(set.getLong("user_id"));
                ModerationStatus status = ModerationStatus.valueOf(set.getString("status"));

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

                ModerationStatus status = ModerationStatus.valueOf(set.getString("status"));

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

                ModerationStatus status = ModerationStatus.valueOf(set.getString("status"));

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
    public DocumentType findDocumentTypeByName(String name) {

        DocumentType type = null;

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_TYPE_BY_NAME)) {

            ps.setString(1, name);

            ResultSet set = ps.executeQuery();


            if (set.next()) {
                type = new DocumentType();
                type.setId(set.getLong("id"));
                type.setType(set.getString("document_type"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return type;
    }

    @Override
    public List<DocumentType> findAllDocumentTypes() {
        List<DocumentType> types = new ArrayList<>();

        try (Connection connection = DbConnector.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(SELECT_ALL_TYPES);

            while (rs.next()) {
                DocumentType type = new DocumentType();
                type.setId(rs.getLong("id"));
                type.setType(rs.getString("document_type"));
                types.add(type);
            }

            rs.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return types;
    }


    @Override
    public Document save(Document document) {

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_DOCUMENT);
             PreparedStatement statement = connection.prepareStatement(UPDATE_CURRENT_VERSION)) {

            saveEntity(document, connection);

            ps.setLong(1, document.getId());
            ps.setLong(2, document.getType().getId());
            ps.setString(3, document.getDocumentPriority().toString());
            ps.setString(4, document.getStatus().toString());
            ps.setLong(5, document.getUser().getId());

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
            statement.setString(3, version.getStatus().toString());
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

    private DocumentType findDocumentTypeById(Long id, Connection connection) throws SQLException {

        PreparedStatement ps = connection.prepareStatement(SELECT_TYPE_BY_ID);
        ps.setLong(1, id);

        ResultSet set = ps.executeQuery();

        DocumentType type = null;

        if (set.next()) {
            type = new DocumentType();
            type.setId(set.getLong("id"));
            type.setType(set.getString("document_type"));
        }

        return type;
    }

    private List<String> findFiles(Long id, Connection connection) throws SQLException {

        List<String> files = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement(SELECT_FILES);
        ps.setLong(1, id);

        ResultSet set = ps.executeQuery();

        while (set.next()) {
            files.add(set.getString("path"));
        }
        return files;
    }

    private void findEntity(ResultSet set, AbstractEntity entity) throws SQLException {
        LocalDate date = LocalDate.parse(set.getString("creation_date"));
        Directory parentDirectory = findDirectoryById(set.getLong("directory_id"));

        entity.setCreationDate(date);
        entity.setParentDirectory(parentDirectory);
    }

    private void saveFiles(List<String> files, Long versionId, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(INSERT_FILES);

        for (String path : files) {
            statement.setString(1, path);
            statement.setLong(2, versionId);
            statement.executeUpdate();
        }
    }

    private void saveEntity(AbstractEntity entity, Connection connection) throws SQLException {

        PreparedStatement statement;

        if (entity.getParentDirectory() != null) {
            statement = connection.prepareStatement(INSERT_ENTITY_IN_DIRECTORY, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(entity.getCreationDate()));
            statement.setLong(2, entity.getParentDirectory().getId());
        } else {
            statement = connection.prepareStatement(INSERT_ENTITY, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(entity.getCreationDate()));
        }

        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet.next()) {
            entity.setId(resultSet.getLong(1));
        }
    }
}
