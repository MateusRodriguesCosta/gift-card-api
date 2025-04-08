package com.giftcard_app.poc_rest.exception;

public class InvalidGiftCardStateException extends RuntimeException {
    public InvalidGiftCardStateException(String message) {
        super(message);
    }
}
