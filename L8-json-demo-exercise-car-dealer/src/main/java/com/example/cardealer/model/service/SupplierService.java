package com.example.cardealer.model.service;

import com.example.cardealer.model.dto.SupplierBasicInfoDto;
import com.example.cardealer.model.entity.Supplier;

import java.io.IOException;
import java.util.List;

public interface SupplierService {
    void seedSuppliers() throws IOException;

    Supplier findRandomSupplier();

    List<SupplierBasicInfoDto> findAllSuppliersNotFromAbroad();
}
