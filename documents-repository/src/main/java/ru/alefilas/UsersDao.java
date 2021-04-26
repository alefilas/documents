package ru.alefilas;

import ru.alefilas.model.user.User;

public interface UsersDao {

    User findById(Long id);

}
