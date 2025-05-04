package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.PaymentResponseDto;
import com.ecommerce.wehackbackend.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "userId", source = "user.id")
    PaymentResponseDto toDto(Payment payment);
}