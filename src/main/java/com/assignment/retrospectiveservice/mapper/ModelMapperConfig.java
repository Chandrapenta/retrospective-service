package com.assignment.retrospectiveservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating a ModelMapper bean.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates a ModelMapper bean.
     *
     * @return the ModelMapper bean
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
