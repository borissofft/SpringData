package course.springdata.codefirst.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
//@DiscriminatorValue(value = "car") // Only for SINGLE_TABLE
public class Car extends Vehicle {
    private final static String TYPE = "CAR";
    private int seats;
    @OneToOne(mappedBy = "car") // Guided part
    private PlateNumber plateNumber;

    public Car(){
        super(TYPE);
    }

    public Car(String type, String model, BigDecimal price, String fuelType, int seats) {
        super(type, model, price, fuelType);
        this.seats = seats;
    }

    public Car(Long id, String type, String model, BigDecimal price, String fuelType, int seats) {
        super(id, type, model, price, fuelType);
        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public PlateNumber getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(PlateNumber plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Car{");
        sb.append(super.toString());
        sb.append("seats=").append(seats);
        sb.append(", plateNumber=").append(plateNumber);
        sb.append('}');
        return sb.toString();
    }
}
