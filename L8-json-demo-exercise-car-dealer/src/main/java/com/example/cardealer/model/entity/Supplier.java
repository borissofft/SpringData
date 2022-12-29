package com.example.cardealer.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

    private String name;
    private boolean isImporter;

    private Set<Part> parts;

    public Supplier() {

    }

    @Column()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "is_importer")
    public boolean isImporter() {
        return isImporter;
    }

    public void setImporter(boolean importer) {
        isImporter = importer;
    }

//    @OneToMany(mappedBy = "supplier", targetEntity = Part.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER) //PersistentObjectException: detached entity passed to persist: com.example.cardealer.model.entity.Supplier
    @OneToMany(mappedBy = "supplier", targetEntity = Part.class, fetch = FetchType.EAGER)
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
        Supplier supplier = (Supplier) o;
        return name.equals(supplier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
