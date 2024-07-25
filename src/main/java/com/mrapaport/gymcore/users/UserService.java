package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mrapaport.gymcore.common.utils.IntegerUtils.isInt;

@Service
public class UserService {

    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;

    }

    public Optional<User> findByDni(String dni) {
        return repository.findByDni(dni);
    }

    public Optional<User> findByUserId(String userId) {
        var user = repository.findByDni(userId);

        if (user.isEmpty() && isInt(userId)) {
            user = repository.findByPin(Integer.parseInt(userId));
        }

        return user;
    }

    public User registerClient(String username, String dni) {
        if (!User.isValid(repository, username, dni)) {
            throw new IllegalArgumentException("Invalid user info");
        }

        return User.saveNew(repository, username, dni);
    }

    public Page<User> getAllClients(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
