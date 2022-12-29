package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskSeedDto {

    @XmlElement
    private String date;
    @XmlElement
    private BigDecimal price;
    @XmlElement
    private CarByIdDto car;
    @XmlElement
    private MechanicByFirstNameDto mechanic;
    @XmlElement
    private PartBiIdDto part;

    public TaskSeedDto() {

    }

    @NotBlank
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NotNull
    @Min(0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CarByIdDto getCar() {
        return car;
    }

    public void setCar(CarByIdDto car) {
        this.car = car;
    }

    public MechanicByFirstNameDto getMechanic() {
        return mechanic;
    }

    public void setMechanic(MechanicByFirstNameDto mechanic) {
        this.mechanic = mechanic;
    }

    public PartBiIdDto getPart() {
        return part;
    }

    public void setPart(PartBiIdDto part) {
        this.part = part;
    }
}
