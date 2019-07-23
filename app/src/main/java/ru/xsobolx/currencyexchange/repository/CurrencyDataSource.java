package ru.xsobolx.currencyexchange.repository;

import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;

public interface CurrencyDataSource {

    void getCurrencies(GetCurrenciesCallback callback);

    interface GetCurrenciesCallback {

        void onCurrenciesLoaded(List<Currency> currencies);

        void onDataNotAvailable();
    }
}
