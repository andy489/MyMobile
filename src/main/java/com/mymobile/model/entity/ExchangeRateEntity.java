package com.mymobile.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rates")
@Getter
@Setter
@Accessors(chain = true)
public class ExchangeRateEntity {

    @Id
    @NotBlank
    private String currency;

    @NotNull
    private BigDecimal rate;
}
