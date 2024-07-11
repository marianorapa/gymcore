package com.mrapaport.gymcore.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Table(name = "config_params")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigParams extends BaseEntity {

    @Column(name = "param", nullable = false, unique = true, length = 100)
    private String param;

    @Column(name = "numeric_value")
    private Double numericValue;

    @Column(name = "string_value", length = 255)
    private String stringValue;
}
