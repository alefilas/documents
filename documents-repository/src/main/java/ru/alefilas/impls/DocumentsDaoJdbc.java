package ru.alefilas.impls;

import lombok.extern.slf4j.Slf4j;
import ru.alefilas.DocumentsDao;
import ru.alefilas.helper.DbConnector;
import ru.alefilas.model.document.AbstractEntity;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.document.Document;
import ru.alefilas.model.document.DocumentVersion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocumentsDaoJdbc implements DocumentsDao {

    private static final String SELECT_ALL_DOCUMENT_TYPES = "SELECT document_type FROM document_type";
    private static final String INSERT_ENTITY = "INSERT INTO entity (creation_date, directory_id) VALUES (?, ?)";
    private static final String INSERT_DOCUMENT = "INSERT INTO document (id, current_version_id, document_type_id, priority_id, user_id, status_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ID_OF_DOCUMENT_TYPE = "SELECT id FROM ? WHERE ? = ?";
    private static final String INSERT_DOCUMENT_VERSION = "INSERT INTO version (title, description, moderation_status_id, document_id) VALUES (?, ?, ?, ?)";
    private static final String INSERT_DIRECTORY = "INSERT INTO directory (id, title) VALUES (?, ?)";
    private static final String SELECT_ALL_BY_DIRECTORY = "SELECT * FROM entity JOIN directory ON (entity.id = directory.id) JOIN document ON entity.id = document.id WHERE directory_id = ?";

    @Override
    public List<String> findAllDocumentTypes() {

        List<String> types = new ArrayList<>();

        try (Connection connection = DbConnector.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(SELECT_ALL_DOCUMENT_TYPES);

            while (rs.next()) {
                types.add(rs.getString(1));
            }

            rs.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return types;
    }

    @Override
    public Long save(AbstractEntity entity) {

        Long id = null;

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ENTITY, Statement.RETURN_GENERATED_KEYS)){

            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setLong(2, entity.getParentDirectory().getId());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
            resultSet.close();

            entity.setId(id);

            if (entity.isDocument()) {
                save((Document) entity, connection);
            } else {
                save((Directory) entity, connection);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return id;

    }

    @Override
    public List<AbstractEntity> findEntityByDirectory(Directory directory) {

        return null;
    }

    @Override
    public void deleteById(String table, Long id) {
        try (Connection connection = DbConnector.getConnection()){
            PreparedStatement ps = connection.prepareStatement("DELETE FROM ? WHERE id = ?");
            ps.setString(1, table);
            ps.setLong(2, id);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Long save(DocumentVersion version) {

        Long id = null;

        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_DOCUMENT_VERSION, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, version.getTitle());
            statement.setString(2, version.getDescription());
            statement.setLong(3, findId("moderation_status", version.getStatus().toString(), connection));
            statement.setLong(4, version.getDocument().getId());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
            resultSet.close();

            version.setId(id);

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return id;
    }

    private void save(Document document, Connection connection) throws SQLException {

        Long version_id = save(document.getCurrentVersion());

        PreparedStatement ps = connection.prepareStatement(INSERT_DOCUMENT);
        ps.setLong(1, document.getId());
        ps.setLong(2, version_id);
        ps.setLong(3, findId("document_type", document.getType(), connection));
        ps.setLong(4, findId("priority", document.getDocumentPriority().toString(), connection));
        ps.setLong(5, document.getUser().getId());
        ps.setLong(6, findId("moderation_status", document.getStatus().toString(), connection));

        ps.executeUpdate();
    }


    private Long findId(String table, String value, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_ID_OF_DOCUMENT_TYPE);
        ps.setString(1, table);
        ps.setString(2, table);
        ps.setString(3, value);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getLong(1);
        } else {
            throw new SQLException("There is no rows with this value:" + value);
        }
    }


    private void save(Directory directory, Connection connection) throws SQLException {

        PreparedStatement ps = connection.prepareStatement(INSERT_DIRECTORY);
        ps.setLong(1, directory.getId());
        ps.setString(2, directory.getTitle());

        ps.executeUpdate();
    }
}
