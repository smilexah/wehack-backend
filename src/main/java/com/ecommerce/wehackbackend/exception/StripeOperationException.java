package com.ecommerce.wehackbackend.exception;

public class StripeOperationException extends PaymentException {
    public StripeOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}