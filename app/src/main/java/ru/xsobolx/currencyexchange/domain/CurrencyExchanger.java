package ru.xsobolx.currencyexchange.domain;

import ru.xsobolx.currencyexchange.domain.model.Currency;

public class CurrencyExchanger {

    public double calculateCourse(Currency from, Currency to) {
        double fromValue = from.getValue() * from.getNominal();
        double toValue = to.getValue() * to.getNominal();
        return (fromValue / toValue);
    }
}
