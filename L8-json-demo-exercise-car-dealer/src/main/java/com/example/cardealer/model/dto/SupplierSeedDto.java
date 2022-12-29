package com.example.cardealer.model.dto;

import com.google.gson.annotations.Expose;

public class SupplierSeedDto {
    @Expose
    private String name;
    @Expose
    private String isImporter;

    public SupplierSeedDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsImporter() {
        return isImporter;
    }

    public void setIsImporter(String isImporter) {
        this.isImporter = isImporter;
    }
}
