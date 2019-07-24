package ru.xsobolx.currencyexchange.presenter;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import ru.xsobolx.currencyexchange.domain.CurrencyExchanger;
import ru.xsobolx.currencyexchange.domain.GetCurrencies;
import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.ui.CurrencyMvpView;

import static androidx.core.util.Preconditions.checkNotNull;

public class CurrencyPresenter {
    private final GetCurrencies getCurrencies;
    private final CurrencyExchanger currencyExchanger;
    private final CurrencyMvpView view;

    private List<Currency> currencies = new ArrayList<>();
    private Currency fromCurrency;
    private Currency toCurrency;
    private double amount = 0.0;

    public CurrencyPresenter(@NonNull GetCurrencies getCurrencies,
                             @NonNull CurrencyExchanger currencyExchanger,
                             @NonNull CurrencyMvpView view) {
        this.getCurrencies = checkNotNull(getCurrencies);
        this.currencyExchanger = checkNotNull(currencyExchanger);
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

    public void onFromCurrencySelected(int pos) {
        fromCurrency = currencies.get(pos);
        if (toCurrency != null) {
            view.setCourse(formatDoubleString(currencyExchanger.calculateCourse(fromCurrency, toCurrency)));
            showCalculatedValue();
        }
    }

    private void showCalculatedValue() {
        view.showCalculatedValue(formatDoubleString(currencyExchanger.calculateCourse(fromCurrency, toCurrency) * amount));
    }

    private String formatDoubleString(double value) {
        BigDecimal bigValue = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
        return bigValue.toEngineeringString();
    }

    public void onToCurrencySelected(int pos) {
        toCurrency = currencies.get(pos);
        if (fromCurrency != null) {
            view.setCourse(formatDoubleString(currencyExchanger.calculateCourse(fromCurrency, toCurrency)));
            showCalculatedValue();
        }
    }

    public void onAmountChanged(CharSequence charSequence) {
        if (charSequence.length() < 1) {
            amount = 0.0;
            view.showCalculatedValue(formatDoubleString(amount));
            return;
        }
        amount = Double.parseDouble(charSequence.toString());
        if (fromCurrency != null && toCurrency != null) {
            showCalculatedValue();
        } else {
            view.showError();
        }
    }
}
