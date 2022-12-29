package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarSeedDto {

    @XmlElement
    private String carMake;
    @XmlElement
    private String carModel;
    @XmlElement
    private int year;
    @XmlElement
    private String plateNumber;
    @XmlElement
    private int kilometers;
    @XmlElement
    private double engine;
    @XmlElement
    private CarType carType;

    public CarSeedDto() {

    }

    @NotBlank
    @Size(min = 2, max = 30)
    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    @NotBlank
    @Size(min = 2, max = 30)
    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    @NotNull
    @Min(0)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @NotBlank
    @Size(min = 2, max = 30)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @NotNull
    @Min(0)
    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    @NotNull
    @DecimalMin(value = "1.00")
    public double getEngine() {
        return engine;
    }

    public void setEngine(double engine) {
        this.engine = engine;
    }

    @NotNull
    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}
