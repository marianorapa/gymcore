package com.mrapaport.gymcore.users;

import com.mrapaport.gymcore.payments.PaymentPlanService;
import com.mrapaport.gymcore.payments.model.PaymentPlan;
import com.mrapaport.gymcore.users.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static com.mrapaport.gymcore.common.utils.IntegerUtils.isInt;

@Service
public class UserService {

    UserRepository repository;

    PaymentPlanService paymentPlanService;

    public UserService(UserRepository repository, PaymentPlanService paymentPlanService) {
        this.repository = repository;
        this.paymentPlanService = paymentPlanService;
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

    public User registerClient(String username, String dni, UUID paymentPlanId, String phoneNumber, UUID promotionId) {
        if (!User.isValid(repository, dni)) {
            throw new IllegalArgumentException("Invalid user info");
        }

        var optPlan = paymentPlanService.findById(paymentPlanId);
        return optPlan.map(plan -> {
            var newUser = User.saveNew(repository, username, dni, plan, phoneNumber);    
            if (promotionId != null) {
                paymentPlanService.addUserPromotion(newUser, promotionId, LocalDate.now().plusMonths(1));
            }

            return newUser;
        }).orElseThrow();
    }

    public Page<User> getAllClients(Pageable pageable, String search) {
        if (search != null && !search.isEmpty()) {
            return repository.findAllByUsernameContainingIgnoreCaseOrDniContainingIgnoreCase(search, search, pageable);
        }
        return repository.findAll(pageable);
    }

    public User findById(UUID id) {
        return enrichedWithPaymentPlan(repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found")));
    }

    private User enrichedWithPaymentPlan(User user) {
        var currentPlan = user.getPaymentPlan();
        var currentCost = paymentPlanService.getCurrentPlanCost(currentPlan);
        currentPlan.setCurrentCost(currentCost.getAmount());
        return user;
    }

    public void updateUser(UUID id, User userUpdates, String promotionId, LocalDate promotionEndDate) {
        var user = findById(id);
        user.setDni(userUpdates.getDni());
        user.setUsername(userUpdates.getUsername());
        user.setPaymentPlan(userUpdates.getPaymentPlan());
        if (promotionId != null && !promotionId.isBlank()) {
            paymentPlanService.addUserPromotion(user, UUID.fromString(promotionId), promotionEndDate);
        }
        repository.save(user);
    }
}
