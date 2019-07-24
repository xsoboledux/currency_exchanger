package ru.xsobolx.currencyexchange.domain;

import org.junit.Test;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.test_data.TestCurrencies;

import static org.junit.Assert.*;

public class CurrencyExchangerTest {

    private final CurrencyExchanger currencyExchanger = new CurrencyExchanger();

    @Test
    public void testExchange() {
        final Currency from = TestCurrencies.getTestCurrency1();
        final Currency to = TestCurrencies.getTestCurrency2();
        final double expected = 0.5;

        assertEquals(expected, currencyExchanger.calculateCourse(from, to), 0);
    }
}