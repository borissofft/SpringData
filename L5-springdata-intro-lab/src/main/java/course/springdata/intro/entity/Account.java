package course.springdata.intro.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
//    private Long id;
    @NonNull
    private BigDecimal Balance;
    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id") // by default
    @ToString.Exclude // We have to remove User from toString because it is included in its class and an infinite loop will result!!!!
    private User user;

}

/**
 * We have to remove User from toString because it is included in its class and an infinite loop will result!!!!
 */