package entities.hospital;


import entities.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

//@Entity
@Table(name = "prescribed_medicaments")
public class PrescribedMedicaments extends BaseEntity {

    private List<Medicament> medicaments;

    private Patient patient;

    public PrescribedMedicaments() {
        this.medicaments = new ArrayList<>();
    }

    @OneToMany(mappedBy = "prescribedMedicaments", targetEntity = Medicament.class)
    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    @ManyToOne
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
