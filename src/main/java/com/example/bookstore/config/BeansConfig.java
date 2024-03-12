package com.example.bookstore.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author oksanapoliakova on 12.03.2024
 * @projectName BookStore
 */
@Component
public class BeansConfig {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
