package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alefilas.model.document.Directory;
import ru.alefilas.model.permit.Permit;
import ru.alefilas.model.user.User;

import java.util.Optional;

@Repository
public interface PermitRepository extends JpaRepository<Permit, Long> {
    @Query("SELECT p " +
                "FROM Permit p " +
                "JOIN p.user u " +
            "WHERE u.username = :username " +
                "AND p.directory = :directory " +
                "AND (p.permitType = 'READ' OR p.permitType = 'WRITE' OR p.permitType = 'MODERATE')")
    Optional<Permit> findReadPermit(String username, Directory directory);

    @Query("SELECT p " +
                "FROM Permit p " +
                "JOIN p.user u " +
            "WHERE u.username = :username " +
                "AND p.directory = :directory " +
                "AND (p.permitType = 'WRITE' OR p.permitType = 'MODERATE')")
    Optional<Permit> findWritePermit(String username, Directory directory);

    @Query("SELECT p " +
                "FROM Permit p " +
                "JOIN p.user u " +
            "WHERE u.username = :username " +
                "AND p.directory = :directory " +
                "AND p.permitType = 'MODERATE'")
    Optional<Permit> findModeratePermit(String username, Directory directory);

    Optional<Permit> findByDirectoryAndUser(Directory directory, User user);
}
