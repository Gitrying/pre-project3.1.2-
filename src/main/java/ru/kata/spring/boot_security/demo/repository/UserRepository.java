package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u FROM User u join fetch u.roles where u.email =:email")
    User findUserByEmail(String email);
}
