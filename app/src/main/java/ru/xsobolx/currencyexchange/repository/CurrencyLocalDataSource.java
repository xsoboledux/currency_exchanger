package ru.xsobolx.currencyexchange.repository;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.persistence.CurrencyDAO;

public class CurrencyLocalDataSource implements ICurrencyLocalDatSource {
    private static CurrencyLocalDataSource INSTANCE = null;

    private final CurrencyDAO currencyDAO;
    private final Executor ioExecutor;
    private final Executor mainThreadExecutor;

    private CurrencyLocalDataSource(@NonNull final CurrencyDAO currencyDAO,
                                    @NonNull final Executor ioExecutor,
                                    @NonNull final Executor mainThreadExecutor) {
        this.ioExecutor = ioExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
        this.currencyDAO = currencyDAO;
    }

    public static CurrencyLocalDataSource getInstance(@NonNull final CurrencyDAO currencyDAO,
                                                      @NonNull final Executor ioExecutor,
                                                      @NonNull final Executor mainThreadExecutor) {
        if (INSTANCE == null) {
            synchronized (CurrencyLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrencyLocalDataSource(currencyDAO, ioExecutor, mainThreadExecutor);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getCurrencies(@NonNull final GetCurrenciesCallback callback) {
        Runnable ioRunnable = new Runnable() {
            @Override
            public void run() {
                final List<Currency> currencies = currencyDAO.getCurrencies();
                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (currencies.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onCurrenciesLoaded(currencies);
                        }
                    }
                });

            }
        };
        ioExecutor.execute(ioRunnable);
    }

    public void saveCurrency(@NonNull final Currency currency) {
        currencyDAO.saveCurrency(currency);
    }

    @Override
    public void deleteAll() {
        currencyDAO.deleteAll();
    }
}
