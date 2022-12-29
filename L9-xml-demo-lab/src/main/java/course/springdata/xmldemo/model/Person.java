package course.springdata.xmldemo.model;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)  // All private or not private fields will be XmlElement type by default evan we don't annotate them
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Person {

    @XmlAttribute(required = true)
    private Long id;
    @NonNull
    private String firstName; // @XmlElement - by default because of annotation @XmlAccessorType(XmlAccessType.FIELD)
    @NonNull
    private String lastName;// @XmlElement - by default because of annotation @XmlAccessorType(XmlAccessType.FIELD)
    @NonNull
    private Address address;// @XmlElement - by default because of annotation @XmlAccessorType(XmlAccessType.FIELD)
    @XmlElementWrapper(name = "phoneNumbers")
    @XmlElement(name = "phone")
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

}
