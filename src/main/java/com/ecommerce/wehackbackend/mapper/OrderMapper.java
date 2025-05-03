package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.OrderResponseDto;
import com.ecommerce.wehackbackend.model.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setEventId(order.getEvent().getId()); // Assuming event is fetched
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCurrency(order.getCurrency());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
