package ru.xsobolx.currencyexchange.domain.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root(name = "Valute")
public class Currency {
    @Element(name = "NumCode")
    public int numCode;
    @Element(name = "CharCode")
    public String charCode;
    @Element(name = "Nominal")
    public int nominal;
    @Element(name = "Name")
    public String name;
    @Element(name = "Value")
    public String value;

    public Double getValue() {
        return Double.parseDouble(value.replace(",", "."));
    }

    public int getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return numCode == currency.numCode &&
                nominal == currency.nominal &&
                Objects.equals(charCode, currency.charCode) &&
                Objects.equals(name, currency.name) &&
                Objects.equals(value, currency.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCode, charCode, nominal, name, value);
    }

    @Override
    public String toString() {
        return charCode;
    }
}
