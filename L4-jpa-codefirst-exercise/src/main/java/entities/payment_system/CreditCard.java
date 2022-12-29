package entities.payment_system;

import javax.persistence.*;

//@Entity
@Table(name = "credit_cards")
//@DiscriminatorValue(value = "credit_cards") // Only for InheritanceType.SINGLE_TABLE
public class CreditCard extends BillingDetail {

    private CardType cardType;
    private int expirationMonth;
    private int expirationYear;

    public CreditCard() {

    }

    @Enumerated(EnumType.STRING) // When use Enumeration you can't place @Column annotation
    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
    @Column(name = "expiration_month", nullable = false)
    public int getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(int expirationMonth) {
        this.expirationMonth = expirationMonth;
    }
    @Column(name = "expiration_year", nullable = false)
    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }
}
