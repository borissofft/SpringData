package course.springdata.jpaintro.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 *  Using Lombok
 */

@Entity // How Java see the class
@Table(name = "students")// How SQL see the class, with it SQL will create table with the name of the class with "s" at the end - "students".
// Lombok - create getters, setters, constructors...
@Data
@NoArgsConstructor // empty constructor
@RequiredArgsConstructor // constructor with field name (@NonNull)
@AllArgsConstructor // constructor with all fields
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", length = 50)
    @NonNull // -> @RequiredArgsConstructor from Lombok
    private String name;
    @Column(name = "registration_date")
    private Date registrationDate = new Date();

}
