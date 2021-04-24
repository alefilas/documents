package ru.alefilas;

import ru.alefilas.model.document.DocumentPriority;
import ru.alefilas.model.moderation.ModerationStatus;
import ru.alefilas.model.user.Role;

import java.sql.SQLException;
import java.util.List;

public interface EnumsDao {

    Role findRoleById(Long id) throws SQLException;

    ModerationStatus findModerationStatusById(Long id) throws SQLException;

    DocumentPriority findPriorityById(Long id) throws SQLException;

    String findDocumentTypeById(Long id) throws SQLException;

    Long findIdOfRole(Role role) throws SQLException;

    Long findIdOfModerationStatus(ModerationStatus status) throws SQLException;

    Long findIdOfPriority(DocumentPriority priority) throws SQLException;

    Long findIdOfDocumentType(String type) throws SQLException;

    List<String> findAllDocumentTypes();

}
