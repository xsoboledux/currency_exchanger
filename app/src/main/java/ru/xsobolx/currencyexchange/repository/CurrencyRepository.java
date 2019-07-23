package ru.xsobolx.currencyexchange.repository;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;

public class CurrencyRepository implements CurrencyDataSource {
    private static CurrencyRepository INSTANCE = null;

    private final CurrencyDataSource remoteDataSource;
    private final ICurrencyLocalDatSource localDataSource;

    private CurrencyRepository(@NonNull CurrencyDataSource remoteDataSource,
                               @NonNull ICurrencyLocalDatSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static CurrencyRepository getInstance(@NonNull CurrencyDataSource currencyRemoteDataSource,
                                                 @NonNull ICurrencyLocalDatSource currencyLocalDataSource) {

        if (INSTANCE == null) {
            synchronized (CurrencyRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrencyRepository(currencyRemoteDataSource, currencyLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCurrencies(final GetCurrenciesCallback callback) {
        remoteDataSource.getCurrencies(new GetCurrenciesCallback() {
            @Override
            public void onCurrenciesLoaded(List<Currency> currencies) {
                localDataSource.deleteAll();
                for (Currency currency : currencies) {
                    localDataSource.saveCurrency(currency);
                }
                callback.onCurrenciesLoaded(currencies);
            }

            @Override
            public void onDataNotAvailable() {
                localDataSource.getCurrencies(new GetCurrenciesCallback() {
                    @Override
                    public void onCurrenciesLoaded(List<Currency> currencies) {
                        callback.onCurrenciesLoaded(currencies);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }
}
