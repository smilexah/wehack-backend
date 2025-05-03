package com.ecommerce.wehackbackend.exception;

public class NotEnoughTicketsException extends RuntimeException {
    public NotEnoughTicketsException(String format) {
        super(format);
    }
}
