package ru.alefilas.impls;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.UsersDao;
import ru.alefilas.model.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UsersDaoJpa implements UsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
}
