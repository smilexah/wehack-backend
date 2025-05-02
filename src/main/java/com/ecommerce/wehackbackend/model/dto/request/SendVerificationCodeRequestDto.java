package com.ecommerce.wehackbackend.model.dto.request;

import lombok.Data;

@Data
public class SendVerificationCodeRequestDto {
    private String email;
}
