package ru.xsobolx.currencyexchange.ui;

import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;

public interface CurrencyMvpView {

    void showLoading();

    void hideLoading();

    void setCourse(String course);

    void showError();

    void showEmptyCurrencies();

    void showCurrencies(List<Currency> response);

    void showCalculatedValue(String result);
}
