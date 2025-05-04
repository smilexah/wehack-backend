package com.ecommerce.wehackbackend.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, Throwable cause) {
        super(message);
    }
}