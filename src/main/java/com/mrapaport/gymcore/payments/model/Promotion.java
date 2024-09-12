package com.mrapaport.gymcore.payments.model;

import java.math.BigDecimal;
import java.util.List;
import com.mrapaport.gymcore.common.BaseEntity;
import com.mrapaport.gymcore.users.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "promotions")
@Data
public class Promotion extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_percentage", nullable = false)
    private BigDecimal discountPercentage;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "max_usage_count", nullable = false)
    private Integer maxUsageCount;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionAssignment> promotionAssignments;

    public Double calculateNetCost(Double grossPlanCost) {
        return grossPlanCost * (100 - discountPercentage.doubleValue()) / 100;
    }

}
