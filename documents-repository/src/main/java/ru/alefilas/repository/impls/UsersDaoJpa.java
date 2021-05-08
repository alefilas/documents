package ru.alefilas.repository.impls;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.repository.UsersDao;
import ru.alefilas.model.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UsersDaoJpa implements UsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
}
