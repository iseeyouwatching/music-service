package ru.hits.musicservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Маппер.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    /**
//     * Шифровальщик паролей.
//     */
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
