package com.giftcard_app.poc_rest.services;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

import java.security.SecureRandom;

public class GiftCardService {

    private static final String DIGITS = "0123456789";
    private static final int BASE_NUMBER_LENGTH = 15;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final LuhnCheckDigit luhn = new LuhnCheckDigit();

    /**
     * Generate gift card number from a random base and the check digit
     * @return Full gift card number
     */
    public String generateGiftCardNumber() throws CheckDigitException {
        StringBuilder baseNumber = new StringBuilder();

        for (int i = 0; i < BASE_NUMBER_LENGTH; i++) {
            int index = RANDOM.nextInt(DIGITS.length());
            baseNumber.append(DIGITS.charAt(index));
        }

        return this.appendCheckDigit(baseNumber.toString());
    }

    public boolean isValidGiftCardNumber(String giftCardNumber) {
        return luhn.isValid(giftCardNumber);
    }

    public String appendCheckDigit(String baseNumber) throws CheckDigitException {
        return baseNumber + luhn.calculate(baseNumber);
    }

}
