package entities.hospital;

import entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private LocalDate dateOfBirth;
    private byte[] picture;
    private boolean haveInsurance;
    private Set<Visitation> visitations;
    private Set<Diagnose> diagnoses;

    private List<PrescribedMedicaments> personalMedicaments;

    public Patient() {

    }

    public Patient(String firstName, String lastName, String address, String email, LocalDate dateOfBirth, byte[] picture, boolean haveInsurance) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAddress(address);
        this.setEmail(email);
        this.setDateOfBirth(dateOfBirth);
        this.setPicture(picture);
        this.setHaveInsurance(haveInsurance);
    }

    @Column(name = "first_name", nullable = false, length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        Validator.validateString(firstName);
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false, length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        Validator.validateString(lastName);
        this.lastName = lastName;
    }

    @Column(nullable = false, length = 120)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        Validator.validateString(address);
        this.address = address;
    }

    @Column(nullable = false, length = 60)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Validator.validateString(email);
        this.email = email;
    }

    @Column(name = "date_of_birth", nullable = false)
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Lob
    @Column(name = "picture", nullable = false)
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        if (picture.length > 50000) {
            throw new IllegalArgumentException("Picture is too long");
        }
        this.picture = picture;
    }

    @Column(name = "insurance", nullable = false)
    public boolean isHaveInsurance() {
        return haveInsurance;
    }

    public void setHaveInsurance(boolean haveInsurance) {
        this.haveInsurance = haveInsurance;
    }

    @OneToMany(mappedBy = "patient", targetEntity = Visitation.class)
    public Set<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(Set<Visitation> visitations) {
        this.visitations = visitations;
    }

    @OneToMany(mappedBy = "patient", targetEntity = Diagnose.class)
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @OneToMany(mappedBy = "patient", targetEntity = PrescribedMedicaments.class)
    public List<PrescribedMedicaments> getPersonalMedicaments() {
        return personalMedicaments;
    }

    public void setPersonalMedicaments(List<PrescribedMedicaments> personalMedicaments) {
        this.personalMedicaments = personalMedicaments;
    }
}
