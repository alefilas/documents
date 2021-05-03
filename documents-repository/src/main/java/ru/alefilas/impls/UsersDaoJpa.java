package ru.alefilas.impls;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.alefilas.UsersDao;
import ru.alefilas.model.user.User;

@Component
public class UsersDaoJpa implements UsersDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public User findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }
}
