package com.example.cardealer.model.dto;

import com.google.gson.annotations.Expose;

public class SupplierBasicInfoDto {
    @Expose
    private Long Id;
    @Expose
    private String Name;
    @Expose
    private Integer partsCount;

    public SupplierBasicInfoDto() {

    }

    public SupplierBasicInfoDto(Long id, String name, Integer partsCount) {
        Id = id;
        this.Name = name;
        this.partsCount = partsCount;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Integer getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(Integer partsCount) {
        this.partsCount = partsCount;
    }
}
