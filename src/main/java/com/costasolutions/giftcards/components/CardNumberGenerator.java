package com.costasolutions.giftcards.components;

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {
    private static final String DIGITS = "0123456789";
    private static final int BASE_NUMBER_LENGTH = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final LuhnCheckDigit luhn = new LuhnCheckDigit();

    /**
     * Generate gift card number from a random base and the check digit
     * @return Full gift card number
     */
    public String generateCardNumber() {
        StringBuilder baseNumber = new StringBuilder(BASE_NUMBER_LENGTH);
        for (int i = 0; i < BASE_NUMBER_LENGTH; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            baseNumber.append(DIGITS.charAt(index));
        }
        return appendCheckDigit(baseNumber.toString());
    }

    /**
     * Append the check digit calculated via the Luhn algorithm to the base number
     * @param baseNumber the generated base number.
     * @return The full card number including the check digit.
     */
    private String appendCheckDigit(String baseNumber) {
        try {
            String checkDigit = luhn.calculate(baseNumber);
            return baseNumber + checkDigit;
        } catch (CheckDigitException e) {
            throw new RuntimeException("Error generating check digit for card number", e);
        }
    }
}
