package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByDni(String dni);
    Optional<User> findByPin(Integer pin);
    Optional<User> findByUsername(String username);
    Page<User> findAllByUsernameContainingIgnoreCaseOrDniContainingIgnoreCase(String username,
            String dni, Pageable pageable);

}
