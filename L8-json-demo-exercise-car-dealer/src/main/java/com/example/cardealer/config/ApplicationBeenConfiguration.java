package com.example.cardealer.config;

import com.example.cardealer.model.dto.SupplierBasicInfoDto;
import com.example.cardealer.model.entity.Supplier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeenConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom converter from String to LocalDateTime
        Converter<String, LocalDateTime> localDateTimeConverter = new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
                return LocalDateTime.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        };

        // Custom converter from LocalDateTime to String
        Converter<LocalDateTime, String> stringConverter = new Converter<LocalDateTime, String>() {
            @Override
            public String convert(MappingContext<LocalDateTime, String> context) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDateTime = context.getSource().format(formatter);
                return formattedDateTime;
            }
        };

        modelMapper.addConverter(localDateTimeConverter);
        modelMapper.addConverter(stringConverter);

        // Custom type map of fields with no matching names
//        TypeMap<Supplier, SupplierBasicInfoDto> supplierTypeMap = modelMapper.createTypeMap(Supplier.class, SupplierBasicInfoDto.class)
//                .addMappings(mapper -> mapper.map(Supplier::getParts, SupplierBasicInfoDto::setPartsCount));

        return modelMapper;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

}
