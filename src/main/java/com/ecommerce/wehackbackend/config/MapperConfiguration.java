package com.ecommerce.wehackbackend.config;

import com.ecommerce.wehackbackend.mapper.ClubMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public ClubMapper clubMapper() {
        return Mappers.getMapper(ClubMapper.class);
    }
}