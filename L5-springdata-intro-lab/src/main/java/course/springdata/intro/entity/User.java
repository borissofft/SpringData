package course.springdata.intro.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
//    private Long id;
    @Column(unique = true)
    @NonNull
    private String username;
    @Column(nullable = false)
    @NonNull
    private int age;
    @OneToMany(mappedBy = "user", targetEntity = Account.class)
    private Set<Account> accounts = new HashSet<>();

}


