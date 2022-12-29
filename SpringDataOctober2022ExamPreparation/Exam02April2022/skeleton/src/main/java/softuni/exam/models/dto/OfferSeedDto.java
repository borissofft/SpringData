package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {
    @XmlElement
    private BigDecimal price;
    @XmlElement
    private AgentByNameDto agent;
    @XmlElement
    private ApartmentByIdDto apartment;
    @XmlElement
    private String publishedOn;

    public OfferSeedDto() {

    }

    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AgentByNameDto getAgent() {
        return agent;
    }

    public void setAgent(AgentByNameDto agent) {
        this.agent = agent;
    }

    public ApartmentByIdDto getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentByIdDto apartment) {
        this.apartment = apartment;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }
}
