package course.springdata.advanced.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "labels")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    private String title;
    private String subtitle;
    @ToString.Exclude
    @OneToMany(mappedBy = "label", targetEntity = Shampoo.class)
    private Set<Shampoo> shampoos;

}
