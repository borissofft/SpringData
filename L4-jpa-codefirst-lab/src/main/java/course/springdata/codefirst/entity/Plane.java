package course.springdata.codefirst.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "planes")
//@DiscriminatorValue(value = "plane") // Only for SINGLE_TABLE
public class Plane extends Vehicle {
    private final static String TYPE = "PLANE";
    @Column(name = "passenger_capacity")
    private int passengerCapacity;

    public Plane() {
        super(TYPE);
    }

    public Plane(String type, String model, BigDecimal price, String fuelType, int passengerCapacity) {
        super(type, model, price, fuelType);
        this.passengerCapacity = passengerCapacity;
    }

    public Plane(Long id, String type, String model, BigDecimal price, String fuelType, int passengerCapacity) {
        super(id, type, model, price, fuelType);
        this.passengerCapacity = passengerCapacity;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Plane{");
        sb.append(super.toString());
        sb.append("passengerCapacity=").append(passengerCapacity);
        sb.append('}');
        return sb.toString();
    }

}
