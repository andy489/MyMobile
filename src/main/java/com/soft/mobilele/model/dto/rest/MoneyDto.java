package com.soft.mobilele.model.dto.rest;

import java.math.BigDecimal;

public record MoneyDto(String currency, BigDecimal amount) {
}
