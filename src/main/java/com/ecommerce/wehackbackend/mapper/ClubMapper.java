package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.request.ClubRequestDto;
import com.ecommerce.wehackbackend.model.dto.response.ClubResponseDto;
import com.ecommerce.wehackbackend.model.entity.Club;
import com.ecommerce.wehackbackend.model.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClubMapper {

    Club toEntity(ClubRequestDto dto);

    ClubResponseDto toDto(Club entity);

    void updateEntity(ClubRequestDto dto, @MappingTarget Club entity);

    default ClubResponseDto.EventDTO eventToDto(Event event) {
        if (event == null) return null;

        ClubResponseDto.EventDTO dto = new ClubResponseDto.EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDate(event.getDate());
        return dto;
    }
}