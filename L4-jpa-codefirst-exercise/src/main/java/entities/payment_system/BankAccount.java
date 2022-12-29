package entities.payment_system;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
@Table(name = "bank_accounts")
//@DiscriminatorValue(value = "bank_accounts")
public class BankAccount extends BillingDetail {

    private String bankName;
    private String swift;

    public BankAccount() {

    }

    @Column(name = "bank_name", nullable = false)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(nullable = false)
    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
