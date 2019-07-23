package ru.xsobolx.currencyexchange.presenter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.xsobolx.currencyexchange.domain.GetCurrencies;
import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.ui.CurrencyMvpView;

import static androidx.core.util.Preconditions.checkNotNull;

public class CurrencyPresenter {
    private final GetCurrencies getCurrencies;
    private final CurrencyMvpView view;

    private List<Currency> currencies = new ArrayList<>();
    private Currency topCurrency;
    private Currency bottomCurrency;
    private double amount = 0.0;

    public CurrencyPresenter(@NonNull GetCurrencies getCurrencies,
                             @NonNull CurrencyMvpView view) {
        this.getCurrencies = checkNotNull(getCurrencies);
        this.view = checkNotNull(view);
    }


    public void onAttach() {
        view.showLoading();
        loadCurrencies();
    }

    private void loadCurrencies() {
        getCurrencies.run(new GetCurrencies.UseCaseCallback<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> response) {
                view.hideLoading();
                if (response.isEmpty()) {
                    view.showEmptyCurrencies();
                } else {
                    currencies = response;
                    view.showCurrencies(response);
                }
            }

            @Override
            public void onError() {
                view.hideLoading();
                view.showError();
            }
        });
    }

    private double calculateCourse(Currency from, Currency to) {
        double fromValue = from.getValue() * from.getNominal();
        double toValue = to.getValue() * to.getNominal();
        return (fromValue / toValue);
    }

    public void onTopCurrencySelected(int pos) {
        topCurrency = currencies.get(pos);
        if (bottomCurrency != null) {
            view.setCourse(String.valueOf(calculateCourse(topCurrency, bottomCurrency)));
            showCalculatedValue();
        }
    }

    private void showCalculatedValue() {
        view.showCalculatedValue(String.valueOf(calculateCourse(topCurrency, bottomCurrency) * amount));
    }

    public void onBottomCurrencySelected(int pos) {
        bottomCurrency = currencies.get(pos);
        if (topCurrency != null) {
            view.setCourse(String.valueOf(calculateCourse(topCurrency, bottomCurrency)));
            showCalculatedValue();
        }
    }

    public void onAmountChanged(CharSequence charSequence) {
        if (charSequence.length() < 1) {
            amount = 0.0;
            view.showCalculatedValue(String.valueOf(amount));
            return;
        }
        amount = Double.parseDouble(charSequence.toString());
        if (topCurrency != null && bottomCurrency != null) {
            showCalculatedValue();
        } else {
            view.showError();
        }
    }
}
