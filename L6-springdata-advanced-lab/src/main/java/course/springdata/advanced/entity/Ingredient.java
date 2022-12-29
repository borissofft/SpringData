package course.springdata.advanced.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@Table(name = "ingredients")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    private String name;
    private BigDecimal price;
    @ToString.Exclude
    @ManyToMany(mappedBy = "ingredients", targetEntity = Shampoo.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Shampoo> shampoos;

}
