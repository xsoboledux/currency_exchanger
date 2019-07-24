package ru.xsobolx.currencyexchange.di;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import ru.xsobolx.currencyexchange.domain.CurrencyExchanger;
import ru.xsobolx.currencyexchange.domain.GetCurrencies;
import ru.xsobolx.currencyexchange.network.HttpClient;
import ru.xsobolx.currencyexchange.network.XmlParser;
import ru.xsobolx.currencyexchange.persistence.CurrencyDAO;
import ru.xsobolx.currencyexchange.persistence.Database;
import ru.xsobolx.currencyexchange.presenter.CurrencyPresenter;
import ru.xsobolx.currencyexchange.repository.CurrencyLocalDataSource;
import ru.xsobolx.currencyexchange.repository.CurrencyRemoteDataSource;
import ru.xsobolx.currencyexchange.repository.CurrencyRepository;
import ru.xsobolx.currencyexchange.ui.CurrencyMvpView;
import ru.xsobolx.currencyexchange.util.IoExecutor;
import ru.xsobolx.currencyexchange.util.MainThreadExecutor;

public class DependencyInjection {

    public static Executor mainThreadExecutor() {
        return MainThreadExecutor.getInstance();
    }

    public static Executor ioExecutor() {
        return IoExecutor.getInstance();
    }

    public static CurrencyRepository currencyRepository(Context context) {
        return CurrencyRepository.getInstance(
                CurrencyRemoteDataSource.getInstance(
                        HttpClient.getInstance(),
                        XmlParser.getInstance(),
                        ioExecutor(),
                        mainThreadExecutor()
                ), CurrencyLocalDataSource.getInstance(
                        CurrencyDAO.getInstance(Database.getInstance(context)),
                        ioExecutor(),
                        mainThreadExecutor()));
    }

    public static GetCurrencies getCurrencies(@NonNull Context context) {
        return new GetCurrencies(currencyRepository(context));
    }

    public static CurrencyPresenter currencyPresenter(@NonNull Context context, CurrencyMvpView mvpView) {
        return new CurrencyPresenter(getCurrencies(context), new CurrencyExchanger(), mvpView);
    }
}
