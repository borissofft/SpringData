package softuni.exam.models.dto;

import java.time.LocalDate;

public class CarGeneralInfoDto {
    private String make;
    private String model;
    private Integer kilometers;
    private LocalDate registeredOn;
    private Integer picturesCount;

    public CarGeneralInfoDto() {

    }

    public CarGeneralInfoDto(String make, String model, Integer kilometers, LocalDate registeredOn, Integer picturesCount) {
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
        this.picturesCount = picturesCount;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Integer getPicturesCount() {
        return picturesCount;
    }

    public void setPicturesCount(Integer picturesCount) {
        this.picturesCount = picturesCount;
    }

    @Override
    public String toString() {
        return String.format("Car make - %s, model - %s\n" +
                        "\tKilometers - %d\n" +
                        "\tRegistered on - %s\n" +
                        "\tNumber of pictures - %d\n",
                this.make, this.model, this.kilometers, this.registeredOn, this.picturesCount);
    }
}
