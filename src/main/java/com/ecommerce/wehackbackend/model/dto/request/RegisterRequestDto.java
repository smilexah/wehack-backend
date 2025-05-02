package com.ecommerce.wehackbackend.model.dto.request;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
