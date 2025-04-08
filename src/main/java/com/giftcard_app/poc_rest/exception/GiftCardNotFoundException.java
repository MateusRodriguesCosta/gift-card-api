package com.giftcard_app.poc_rest.exception;

public class GiftCardNotFoundException extends RuntimeException {
    public GiftCardNotFoundException(String message) {
        super(message);
    }
}
