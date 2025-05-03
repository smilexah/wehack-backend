package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.TicketResponseDto;
import com.ecommerce.wehackbackend.model.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDto toDto(Ticket ticket) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setOrderId(ticket.getOrder().getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setQrCode(ticket.getQrCode());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        return dto;
    }
}