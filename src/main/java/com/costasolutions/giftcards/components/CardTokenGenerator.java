package com.costasolutions.giftcards.components;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CardTokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public String generateToken() {
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }
}
