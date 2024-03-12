package com.mymobile.model.validation.offer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = YearAfter1960AndNotFutureValidator.class)
public @interface YearAfter1960AndNotFuture {

    String message() default "invalid year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
