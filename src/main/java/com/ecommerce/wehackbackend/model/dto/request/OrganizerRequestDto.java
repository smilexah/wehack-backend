package com.ecommerce.wehackbackend.model.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrganizerRequestDto(
    @NotNull Long clubId
) {}