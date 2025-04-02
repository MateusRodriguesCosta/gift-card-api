package com.giftcard_app.poc_rest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GiftCardNumberValidator.class)
public @interface ValidGiftCardNumber {
    String message() default "Invalid Gift Card Number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
