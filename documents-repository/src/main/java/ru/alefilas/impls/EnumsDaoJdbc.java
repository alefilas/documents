package ru.alefilas.impls;

import lombok.extern.slf4j.Slf4j;
import ru.alefilas.EnumsDao;
import ru.alefilas.helper.DbConnector;
import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EnumsDaoJdbc implements EnumsDao {

    private static final String SELECT_ID = "SELECT id FROM $tableName WHERE $tableName = ?";
    private static final String SELECT_VALUE = "SELECT $tableName FROM $tableName WHERE id = ?";
    private static final String SELECT_ALL_DOCUMENT_TYPES = "SELECT document_type FROM document_type";


    @Override
    public Role findRoleById(Long id) throws SQLException {
        return Role.valueOf(findValue("role", id));
    }

    @Override
    public ModerationStatus findModerationStatusById(Long id) throws SQLException {
        return ModerationStatus.valueOf(findValue("moderation_status", id));
    }

    @Override
    public DocumentPriority findPriorityById(Long id) throws SQLException {
        return DocumentPriority.valueOf(findValue("priority", id));
    }

    @Override
    public String findDocumentTypeById(Long id) throws SQLException {
        return findValue("document_type", id);
    }

    @Override
    public Long findIdOfRole(Role role) throws SQLException {
        return findId("role", role.toString());
    }

    @Override
    public Long findIdOfModerationStatus(ModerationStatus status) throws SQLException {
        return findId("moderation_status", status.toString());
    }

    @Override
    public Long findIdOfPriority(DocumentPriority priority) throws SQLException {
        return findId("priority", priority.toString());
    }

    @Override
    public Long findIdOfDocumentType(String type) throws SQLException {
        return findId("document_type", type);
    }

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

    private String findValue(String table, Long id) throws SQLException {

        Connection connection = DbConnector.getConnection();
        PreparedStatement ps = connection.prepareStatement(SELECT_VALUE.replaceAll("\\$tableName", table));

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        try {
            if (rs.next()) {
                String s = rs.getString(1);
                return rs.getString(1);
            } else {
                throw new SQLException("There is no rows with this id:" + id);
            }
        } finally {
            connection.close();
        }
    }

    private Long findId(String table, String value) throws SQLException {

        Connection connection = DbConnector.getConnection();
        PreparedStatement ps = connection.prepareStatement(SELECT_ID.replace("$tableName", table));

        ps.setString(1, value);

        ResultSet rs = ps.executeQuery();

        try {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SQLException("There is no rows with this value:" + value);
            }
        } finally {
            connection.close();
        }
    }
}
