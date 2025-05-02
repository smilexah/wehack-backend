package com.ecommerce.wehackbackend.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resource, String field, String value) {
        super(String.format("%s with %s: '%s' already exists", resource, field, value));
    }
}