package entities.demo_hcf;

import entities.BaseEntity;

import javax.persistence.*;

/**
 * Variant 1 - just @MappedSuperclass
 * Variant 2 - @Entity and @Inheritance(strategy = InheritanceType.JOINED)
 */

//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@MappedSuperclass
public abstract class Person extends BaseEntity {

    private String name;

    public Person() {
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
