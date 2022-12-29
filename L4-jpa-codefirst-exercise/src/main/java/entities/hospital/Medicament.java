package entities.hospital;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

//@Entity
@Table(name = "medicaments")
public class Medicament extends BaseEntity {
    private String name;

    private PrescribedMedicaments prescribedMedicaments;

    public Medicament() {

    }

    public Medicament(String name) {
        this.setName(name);
    }


    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateString(name);
        this.name = name;
    }

    @ManyToOne()
    @JoinColumn(name = "prescribed_medicaments_id", referencedColumnName = "id")
    public PrescribedMedicaments getPrescribedMedicaments() {
        return prescribedMedicaments;
    }

    public void setPrescribedMedicaments(PrescribedMedicaments prescribedMedicaments) {
        this.prescribedMedicaments = prescribedMedicaments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicament that = (Medicament) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Medicament{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
