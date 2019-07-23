package ru.xsobolx.currencyexchange.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.test_data.TestCurrencies;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class CurrencyRepositoryTest {

    private CurrencyRepository currencyRepository;
    @Mock
    private ICurrencyLocalDatSource localDatSource;
    @Mock
    private CurrencyDataSource remoteDataSource;
    @Mock
    private CurrencyDataSource.GetCurrenciesCallback getCurrenciesCallback;
    @Captor
    private ArgumentCaptor<CurrencyDataSource.GetCurrenciesCallback> currenciesCallbackArgumentCaptor;

    private List<Currency> currencies = TestCurrencies.getCurrencies();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        currencyRepository = CurrencyRepository.getInstance(remoteDataSource, localDatSource);
    }

    @After
    public void tearDown() {
        reset(localDatSource);
        reset(remoteDataSource);
        reset(getCurrenciesCallback);
        CurrencyRepository.destroyInstance();
    }

    @Test
    public void getCurrencies_fromRemoteDataSource() {
        currencyRepository.getCurrencies(getCurrenciesCallback);

        // set remote currencies available
        verify(remoteDataSource).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onCurrenciesLoaded(currencies);

        verify(getCurrenciesCallback).onCurrenciesLoaded(currencies);
    }

    @Test
    public void getCurrencies_fromLocalDataSource_whenRemoteNotAvailable() {
        currencyRepository.getCurrencies(getCurrenciesCallback);

        // set remote CURRENCIES not available
        verify(remoteDataSource).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        // set local CURRENCIES available
        verify(localDatSource).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onCurrenciesLoaded(currencies);

        verify(getCurrenciesCallback).onCurrenciesLoaded(currencies);
    }

    @Test
    public void getCurrenceis_onDataNotAvailable_onBothSourceNotAvailable() {
        currencyRepository.getCurrencies(getCurrenciesCallback);

        // set remote currencies not available
        verify(remoteDataSource).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        // set local currencies not available
        verify(localDatSource).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        verify(getCurrenciesCallback).onDataNotAvailable();
    }
}