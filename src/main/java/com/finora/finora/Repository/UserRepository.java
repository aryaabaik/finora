package com.finora.finora.Repository;

import com.finora.finora.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:login) OR LOWER(u.email) = LOWER(:login)")
    Optional<User> findByUsernameOrEmailIgnoreCase(@Param("login") String login);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByUsernameContainingIgnoreCase(@Param("keyword") String keyword);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}