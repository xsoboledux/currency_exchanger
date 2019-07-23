package ru.xsobolx.currencyexchange.repository;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import ru.xsobolx.currencyexchange.network.CurrencyApiResponse;
import ru.xsobolx.currencyexchange.network.HttpClient;
import ru.xsobolx.currencyexchange.network.XmlParser;

public class CurrencyRemoteDataSource implements CurrencyDataSource {
    private static CurrencyRemoteDataSource INSTANCE = null;

    private final HttpClient httpClient;
    private final XmlParser xmlParser;
    private final Executor ioExecutor;
    private final Executor mainThreadExecutor;

    private CurrencyRemoteDataSource(@NonNull final HttpClient httpClient,
                                     @NonNull final XmlParser xmlParser,
                                     @NonNull Executor ioExecutor,
                                     @NonNull Executor mainThreadExecutor) {
        this.httpClient = httpClient;
        this.xmlParser = xmlParser;
        this.ioExecutor = ioExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    public static CurrencyRemoteDataSource getInstance(@NonNull final HttpClient httpClient,
                                                       @NonNull final XmlParser xmlParser,
                                                       @NonNull final Executor ioExecutor,
                                                       @NonNull final Executor mainThreadExecutor) {
        if (INSTANCE == null) {
            synchronized (CurrencyRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrencyRemoteDataSource(httpClient, xmlParser, ioExecutor, mainThreadExecutor);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getCurrencies(final GetCurrenciesCallback getCurrenciesCallback) {
        Runnable ioRunnable = new Runnable() {
            @Override
            public void run() {
                String response = httpClient.get("http://www.cbr.ru/scripts/XML_daily.asp");
                mainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response == null) {
                            getCurrenciesCallback.onDataNotAvailable();
                        } else {
                            CurrencyApiResponse apiResponse;
                            try {
                                apiResponse = xmlParser.fromXML(response, CurrencyApiResponse.class);
                                getCurrenciesCallback.onCurrenciesLoaded(apiResponse.getCurrencies());
                            } catch (Exception e) {
                                getCurrenciesCallback.onDataNotAvailable();
                            }
                        }
                    }
                });
            }
        };
        ioExecutor.execute(ioRunnable);
    }
}
