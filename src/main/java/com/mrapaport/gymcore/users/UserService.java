package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.users.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.mrapaport.gymcore.common.utils.IntegerUtils.isInt;

@Service
public class UserService {

    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;

    }

    public Optional<User> findByUserId(String userId) {
        var user = repository.findByDni(userId);

        if (user.isEmpty() && isInt(userId)) {
            user = repository.findByPin(Integer.parseInt(userId));
        }

        return user;
    }

}
