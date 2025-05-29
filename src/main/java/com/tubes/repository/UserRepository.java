package com.tubes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tubes.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
