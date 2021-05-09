package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alefilas.model.user.User;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String username);

}
