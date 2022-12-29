package com.example.cardealer.model.dto;

import com.example.cardealer.model.entity.Sale;
import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;

public class CustomerBasicInfoDto {

    @Expose
    private Long Id;
    @Expose
    private String Name;
    @Expose
    private String birthDate;
    @Expose
    private boolean IsYoungDriver;
    @Expose
    private Set<Sale> Sales;

    public CustomerBasicInfoDto() {

    }

//    public CustomerBasicInfoDto(Long id, String name, String birthDate, boolean isYoungDriver) {
//        Id = id;
//        Name = name;
//        this.birthDate = birthDate;
//        IsYoungDriver = isYoungDriver;
//        this.Sales = new HashSet<>();
//    }


    public CustomerBasicInfoDto(Long id, String name, String birthDate, boolean isYoungDriver) {
        Id = id;
        Name = name;
        this.birthDate = birthDate;
        IsYoungDriver = isYoungDriver;
    }

    public CustomerBasicInfoDto(Long id, String name, String birthDate, boolean isYoungDriver, Set<Sale> sales) {
        Id = id;
        Name = name;
        this.birthDate = birthDate;
        IsYoungDriver = isYoungDriver;
        Sales = sales;
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
        Name = name;
    }

    public boolean isYoungDriver() {
        return IsYoungDriver;
    }

    public void setYoungDriver(boolean youngDriver) {
        IsYoungDriver = youngDriver;
    }

    public Set<Sale> getSales() {
        return Sales;
    }

    public void setSales(Set<Sale> sales) {
        Sales = sales;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
