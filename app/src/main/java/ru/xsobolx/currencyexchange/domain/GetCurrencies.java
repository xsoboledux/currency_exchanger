package ru.xsobolx.currencyexchange.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.repository.CurrencyDataSource;

public class GetCurrencies {
    private final CurrencyDataSource currencyRepository;

    public GetCurrencies(@NonNull CurrencyDataSource currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public void run(@NonNull final UseCaseCallback<List<Currency>> useCaseCallback) {
        currencyRepository.getCurrencies(new CurrencyDataSource.GetCurrenciesCallback() {
            @Override
            public void onCurrenciesLoaded(List<Currency> currencies) {
                UseCaseResponse response = new UseCaseResponse(currencies);
                useCaseCallback.onSuccess(response.getCurrencies());
            }

            @Override
            public void onDataNotAvailable() {
                useCaseCallback.onError();
            }
        });
    }

    public interface UseCaseCallback<T> {

        void onSuccess(T response);

        void onError();
    }

    public static final class UseCaseResponse {
        private final List<Currency> currencies;

        public UseCaseResponse(@NonNull List<Currency> currencies) {
            this.currencies = currencies;
        }

        public List<Currency> getCurrencies() {
            return currencies;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UseCaseResponse that = (UseCaseResponse) o;
            return Objects.equals(currencies, that.currencies);
        }

        @Override
        public int hashCode() {
            return Objects.hash(currencies);
        }
    }
}
