package com.example.dtoexercise.config;

import com.example.dtoexercise.model.dto.GameAddDto;
import com.example.dtoexercise.model.entity.Game;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom converter from String to LocalDate - use it if GameAddDto class have field: private String releaseDate;
        // The other option is to convert it to LocalDate while reading the input data - This is the chosen implementation(other is commented)
        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> context) {
                return context.getSource() == null
                        ? LocalDate.now()
                        : LocalDate.parse(context.getSource(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };

        modelMapper.addConverter(localDateConverter);

        // Custom type map of fields with no matching names from GameAddDto to Game
        modelMapper.typeMap(GameAddDto.class, Game.class)
                .addMappings(mapper ->
                        mapper.map(GameAddDto::getThumbnailURL, Game::setImageThumbnail));

        return modelMapper;
    }

}
