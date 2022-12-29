package com.example.cardealer.model.service.impl;

import com.example.cardealer.model.dto.PartSeedDto;
import com.example.cardealer.model.entity.Part;
import com.example.cardealer.model.entity.Supplier;
import com.example.cardealer.model.repository.PartRepository;
import com.example.cardealer.model.service.PartService;
import com.example.cardealer.model.service.SupplierService;
import com.example.cardealer.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.cardealer.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class PartServiceImpl implements PartService {

    private static final String PARTS_FILE_NAME = "parts.json";
    private final PartRepository partRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    private final SupplierService supplierService;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, SupplierService supplierService) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.supplierService = supplierService;
    }

    @Override
    public void seedParts() throws IOException {

        if (this.partRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + PARTS_FILE_NAME));

        PartSeedDto[] partSeedDtos = this.gson.fromJson(fileContent, PartSeedDto[].class);

        List<Part> partList = Arrays.stream(partSeedDtos)
                .filter(validationUtil::isValid)
                .map(partSeedDto -> {
                    Part part = modelMapper.map(partSeedDto, Part.class);
                    Supplier supplier = this.supplierService.findRandomSupplier();
                    part.setSupplier(supplier);
                    return part;
                }).toList();

        this.partRepository.saveAll(partList);
    }

    @Override
    public Set<Part> findRandomParts() {

        Set<Part> parts = new HashSet<>();

        int catCount = ThreadLocalRandom
                .current().nextInt(1, 3);

        long totalPartsCount = this.partRepository.count();
        for (int i = 0; i < catCount; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, totalPartsCount + 1);
            parts.add(this.partRepository.findById(randomId).orElse(null));
        }

        return parts;
    }
}
