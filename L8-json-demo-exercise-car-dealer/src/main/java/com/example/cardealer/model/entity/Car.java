package com.example.cardealer.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    private String make;
    private String model;
    private Long travelledDistance;
    private Set<Part> parts;

    public Car() {

    }

    @Column
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Column
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "travelled_distance")
    public Long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }
//    @ManyToMany(mappedBy = "cars", targetEntity = Part.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER) // PersistentObjectException: detached entity passed to persist: com.example.cardealer.model.entity.Part
    @ManyToMany(mappedBy = "cars", targetEntity = Part.class, fetch = FetchType.EAGER)
    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return make.equals(car.make);
    }

    @Override
    public int hashCode() {
        return Objects.hash(make);
    }

}
