package com.mymobile.model.validation.offer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class YearAfter1960AndNotFutureValidator implements ConstraintValidator<YearAfter1960AndNotFuture, String> {

    private static final int MIN_YEAR_MANUFACTURED = 1960;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }

        int year;

        try {
            year = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return year <= Year.now().getValue() && year >= MIN_YEAR_MANUFACTURED;
    }
}
