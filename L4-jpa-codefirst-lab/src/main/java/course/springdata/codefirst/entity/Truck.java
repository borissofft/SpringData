package course.springdata.codefirst.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "trucks")
//@DiscriminatorValue(value = "truck") // Only for SINGLE_TABLE
public class Truck extends Vehicle {
    private final static String TYPE = "TRUCK";
    @Column(name = "load_capacity")
    private double loadCapacity;

    public Truck() {
        super(TYPE);
    }

    public Truck(String type, String model, BigDecimal price, String fuelType, double loadCapacity) {
        super(type, model, price, fuelType);
        this.loadCapacity = loadCapacity;
    }

    public Truck(Long id, String type, String model, BigDecimal price, String fuelType, double loadCapacity) {
        super(id, type, model, price, fuelType);
        this.loadCapacity = loadCapacity;
    }

    public double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Truck{");
        sb.append(super.toString());
        sb.append("loadCapacity=").append(loadCapacity);
        sb.append('}');
        return sb.toString();
    }

}
