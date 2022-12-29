package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class CarByIdDto {

    @XmlElement(name = "id")
    private long id;

    public CarByIdDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
