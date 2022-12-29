package course.springdata.codefirst.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity // Java
@Table(name = "vehicles") // DB
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type") // Only for SINGLE_TABLE
public abstract class Vehicle {
    @Id // DB
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB
//    @GeneratedValue(strategy = GenerationType.TABLE) // DB // Use TABLE strategy for mapping TABLE_PER_CLASS
    private Long id;
    @Basic
//    @Column(insertable = false,updatable = false) // Only for SINGLE_TABLE
    private String type;
    private String model;
    private BigDecimal price;
    @Column(name = "fuel_type") // DB column
    private String fuelType;

    protected Vehicle() {
    }

    protected Vehicle(String type) {
        this.type = type;
    }

    protected Vehicle(String type, String model, BigDecimal price, String fuelType) {
        this.type = type;
        this.model = model;
        this.price = price;
        this.fuelType = fuelType;
    }

    protected Vehicle(Long id, String type, String model, BigDecimal price, String fuelType) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.price = price;
        this.fuelType = fuelType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", price=" + price +
                ", fuelType='" + fuelType + '\'' +
                '}';
    }

}
