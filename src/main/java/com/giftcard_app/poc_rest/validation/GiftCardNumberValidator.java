package com.giftcard_app.poc_rest.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class GiftCardNumberValidator implements ConstraintValidator<ValidGiftCardNumber, String> {

    private final LuhnCheckDigit luhn = new LuhnCheckDigit();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return luhn.isValid(value);
    }
}
