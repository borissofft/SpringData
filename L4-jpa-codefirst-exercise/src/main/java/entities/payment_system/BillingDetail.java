package entities.payment_system;

import entities.BaseEntity;

import javax.persistence.*;

//@Entity
@Table(name = "billing_details")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type") // Only for InheritanceType.SINGLE_TABLE
public abstract class BillingDetail extends BaseEntity {

    private String number;
    private User user;

    public BillingDetail() {

    }

    @Column(nullable = false, unique = true)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
