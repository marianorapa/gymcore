package com.mrapaport.gymcore.users.model;

import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.usage.UsageService;
import com.mrapaport.gymcore.users.UserRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(name = "pin", unique = true, nullable = false)
    private Integer pin;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "dni", unique = true, nullable = false)
    private String dni;

    public static User saveNew(UserRepository repository, String username, String dni) {
        var user = new User();
        user.dni = dni;
        user.username = username;
        user.pin = generatePin();
        while (repository.findByPin(user.pin).isPresent()) {
            user.pin = generatePin();
        }
        return repository.save(user);
    }

    private static Integer generatePin() {
        return ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
    }

    public static boolean isValid(UserRepository repository, String username, String dni) {
        return repository.findByDni(dni).isEmpty() && repository.findByUsername(username).isEmpty();
    }

    public boolean hasValidAccess(UsageService usageService) {
        var currentQuota = usageService.getCurrentUsageQuota(this);
        return currentQuota.isPresent();
    }
}
