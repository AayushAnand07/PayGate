package org.gateway.paygate.common.entity;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Money {

    private int amountUnits;
    private String currency;

    protected Money() {
        // required by JPA
    }

    private Money(int amountUnits, String currency) {
        this.amountUnits = amountUnits;
        this.currency = currency;
    }

    public static Money of(int amountUnits, String currency) {
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits) {
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add Money with different currencies");
        }
        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract Money with different currencies");
        }
        if (this.amountUnits < other.amountUnits) {
            throw new IllegalArgumentException("Resulting amount cannot be negative");
        }
        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare Money with different currencies");
        }
        return this.amountUnits > other.amountUnits;
    }

    public boolean isLessThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare Money with different currencies");
        }
        return this.amountUnits < other.amountUnits;
    }

    public int getAmountUnits() {
        return amountUnits;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amountUnits == money.amountUnits && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountUnits, currency);
    }

    @Override
    public String toString() {
        return amountUnits + " " + currency;
    }
}