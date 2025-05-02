package com.ecommerce.wehackbackend.model.dto.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
