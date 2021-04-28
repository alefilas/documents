package ru.alefilas.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alefilas.EnumsDao;
import ru.alefilas.UsersDao;
import ru.alefilas.helper.DbConnector;
import ru.alefilas.model.user.Role;
import ru.alefilas.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
public class UsersDaoJdbc implements UsersDao {

    @Autowired
    private EnumsDao enumsDao;

    private static final String SELECT_USER = "SELECT * FROM users WHERE id = ?";

    @Override
    public User findById(Long id) {

        User user = null;

        try (Connection connection = DbConnector.getConnection()){

            PreparedStatement ps = connection.prepareStatement(SELECT_USER);
            ps.setLong(1, id);

            ResultSet set = ps.executeQuery();

            if (set.next()) {
                user = new User();
                user.setId(set.getLong("id"));
                user.setName(set.getString("name"));
                user.setEmail(set.getString("email"));

                Role role = enumsDao.findRoleById(set.getLong("role_id"));
                user.setRole(role);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return user;
    }
}
