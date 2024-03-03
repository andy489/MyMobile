package com.soft.mobilele.model.dto.rest;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

public record ConvertRequestDto(@NotBlank String target, @NotNull @Positive BigDecimal amount) {
}
