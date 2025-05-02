package com.ecommerce.wehackbackend.exception;

public class FailedToSendVerificationCode extends RuntimeException {
    public FailedToSendVerificationCode(String message) {
        super(message);
    }
}
