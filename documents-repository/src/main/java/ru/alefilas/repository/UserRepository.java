package ru.alefilas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alefilas.model.user.Role;
import ru.alefilas.model.user.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRoleIn(List<Role> roles);

}
