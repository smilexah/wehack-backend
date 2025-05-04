package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.SubscriptionResponse;
import com.ecommerce.wehackbackend.model.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    
    @Mapping(target = "clubId", source = "club.id")
    @Mapping(target = "clubName", source = "club.name")
    @Mapping(target = "userId", source = "user.id")
    SubscriptionResponse toDto(Subscription subscription);
    
    default List<SubscriptionResponse> toDtoList(List<Subscription> subscriptions) {
        return subscriptions.stream()
            .map(this::toDto)
            .toList();
    }
}