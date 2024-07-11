package com.mrapaport.gymcore.payments;

import com.mrapaport.gymcore.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_plans")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPlan extends BaseEntity {

    @Column(name = "plan_name", length = 255)
    private String planName;

    @Column(name = "plan_description", length = 255)
    private String planDescription;

    @Column(name = "percent_discount")
    private Double percentDiscount;
}
