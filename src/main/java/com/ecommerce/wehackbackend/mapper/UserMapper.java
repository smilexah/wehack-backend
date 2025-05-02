package com.ecommerce.wehackbackend.mapper;

import com.ecommerce.wehackbackend.model.dto.response.UserResponseDto;
import com.ecommerce.wehackbackend.model.entity.User;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }

}
