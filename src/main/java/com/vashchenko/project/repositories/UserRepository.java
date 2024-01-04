package com.vashchenko.project.repositories;

import com.vashchenko.project.enums.Role;
import com.vashchenko.project.models.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional findUserByLogin(String login);
    @Query("select u from User u where u.login != :login and u.isRegistered = true")
    Iterable<User> findAllByLoginNot(String login);

    @EntityGraph(attributePaths = {})
    List<User> findAll();
}
