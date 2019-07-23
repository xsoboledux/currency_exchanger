package ru.xsobolx.currencyexchange.repository;

import ru.xsobolx.currencyexchange.domain.model.Currency;

public interface ICurrencyLocalDatSource extends CurrencyDataSource {

    void saveCurrency(Currency currency);

    void deleteAll();
}
