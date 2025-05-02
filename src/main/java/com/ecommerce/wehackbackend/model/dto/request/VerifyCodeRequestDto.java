package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}