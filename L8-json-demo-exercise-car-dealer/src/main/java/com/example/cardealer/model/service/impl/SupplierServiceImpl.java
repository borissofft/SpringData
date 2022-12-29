package com.example.cardealer.model.service.impl;

import com.example.cardealer.model.dto.SupplierBasicInfoDto;
import com.example.cardealer.model.dto.SupplierSeedDto;
import com.example.cardealer.model.entity.Supplier;
import com.example.cardealer.model.repository.SupplierRepository;
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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.cardealer.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class SupplierServiceImpl implements SupplierService {

    private static final String SUPPLIERS_FILE_NAME = "suppliers.json";

    private final SupplierRepository supplierRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.supplierRepository = supplierRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedSuppliers() throws IOException {

        if (this.supplierRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + SUPPLIERS_FILE_NAME));

        SupplierSeedDto[] supplierSeedDtos = this.gson.fromJson(fileContent, SupplierSeedDto[].class);

        Arrays.stream(supplierSeedDtos)
                .filter(validationUtil::isValid)
                .map(supplierSeedDto -> modelMapper.map(supplierSeedDto, Supplier.class))
                .forEach(supplierRepository::save);

    }

    @Override
    public Supplier findRandomSupplier() {

        long randomId = ThreadLocalRandom
                .current().nextLong(1, this.supplierRepository.count() + 1);

        return this.supplierRepository
                .findById(randomId)
                .orElse(null);

    }

    @Override
    public List<SupplierBasicInfoDto> findAllSuppliersNotFromAbroad() {
        return this.supplierRepository.findAllByImporterIsFalse()
                .stream()
                .map(supplier -> {
                    SupplierBasicInfoDto supplierBasicInfoDto = modelMapper.map(supplier, SupplierBasicInfoDto.class);
                    supplierBasicInfoDto.setPartsCount(supplier.getParts().size());
                    return supplierBasicInfoDto;
                })
                .collect(Collectors.toList());
    }


}
