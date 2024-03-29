package ru.xsobolx.currencyexchange.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import ru.xsobolx.currencyexchange.domain.CurrencyExchanger;
import ru.xsobolx.currencyexchange.domain.GetCurrencies;
import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.repository.CurrencyRepository;
import ru.xsobolx.currencyexchange.test_data.TestCurrencies;
import ru.xsobolx.currencyexchange.ui.CurrencyMvpView;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CurrencyPresenterTest {
    @Mock
    private CurrencyMvpView view;
    @Mock
    private CurrencyRepository currencyRepository;
    private CurrencyExchanger currencyExchanger;

    private GetCurrencies getCurrencies;

    @Captor
    private ArgumentCaptor<CurrencyRepository.GetCurrenciesCallback> currenciesCallbackArgumentCaptor;

    private CurrencyPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        currencyExchanger = new CurrencyExchanger();
        getCurrencies = new GetCurrencies(currencyRepository);
        presenter = new CurrencyPresenter(getCurrencies, currencyExchanger, view);
    }

    @Test
    public void shouldShowCurrencies_onAttach_ifNoErrors() {
        List<Currency> currencies = TestCurrencies.getCurrencies();
        presenter.onAttach();

        // set currencies
        verify(currencyRepository).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onCurrenciesLoaded(currencies);

        verify(view, atLeastOnce()).showLoading();
        verify(view, atLeastOnce()).showCurrencies(currencies);
        verify(view, atLeastOnce()).hideLoading();
        verify(view, never()).showError();
    }

    @Test
    public void shouldShowEmptyCurrencies_onAttach_ifNoErrors_onEmptyCurrencies() {
        List<Currency> currencies = Collections.emptyList();
        presenter.onAttach();

        // set empty currencies
        verify(currencyRepository).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onCurrenciesLoaded(currencies);

        verify(view, atLeastOnce()).showLoading();
        verify(view, atLeastOnce()).hideLoading();
        verify(view, atLeastOnce()).showEmptyCurrencies();
        verify(view, never()).showCurrencies(currencies);
        verify(view, never()).showError();
    }

    @Test
    public void shouldShowError_onDataNotAvailable() {
        List<Currency> currencies = TestCurrencies.getCurrencies();
        presenter.onAttach();

        // set error
        verify(currencyRepository).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onDataNotAvailable();

        verify(view, atLeastOnce()).showLoading();
        verify(view, atLeastOnce()).hideLoading();
        verify(view, atLeastOnce()).showError();
        verify(view, never()).showEmptyCurrencies();
        verify(view, never()).showCurrencies(currencies);
    }

    @Test
    public void testCalculateValue() {
        presenter.onAttach();
        int fromCurrencyPosition = 1;
        int toCurrencyPosition = 2;

        // set currencies
        verify(currencyRepository).getCurrencies(currenciesCallbackArgumentCaptor.capture());
        currenciesCallbackArgumentCaptor.getValue().onCurrenciesLoaded(TestCurrencies.getCurrencies());

        // select currencies
        presenter.onFromCurrencySelected(fromCurrencyPosition);
        presenter.onToCurrencySelected(toCurrencyPosition);
        presenter.onAmountChanged("10");

        verify(view, atLeastOnce()).showCalculatedValue(String.valueOf(calculateCourse(fromCurrencyPosition, toCurrencyPosition, 10)));
    }

    private String calculateCourse(int fromPosition, int toPosition, double amount) {
        Currency from = TestCurrencies.getCurrencies().get(fromPosition);
        Currency to = TestCurrencies.getCurrencies().get(toPosition);
        double fromValue = from.getValue() * from.getNominal();
        double toValue = to.getValue() * to.getNominal();
        double course = fromValue / toValue * amount;
        BigDecimal bigValue = new BigDecimal(course).setScale(4, RoundingMode.HALF_UP);
        return bigValue.toEngineeringString();
    }
}