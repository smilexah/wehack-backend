package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClubRequestDto {
    @NotBlank(message = "Club name is required")
    @Size(max = 255, message = "Club name must be less than 255 characters")
    private String name;

    private String description;
}