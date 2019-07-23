package ru.xsobolx.currencyexchange.test_data;

import java.util.ArrayList;
import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.network.CurrencyApiResponse;

public class TestCurrencies {
    public static Currency getTestCurrency1() {
        final Currency currency = new Currency();
        currency.charCode = "c1";
        currency.numCode = 1;
        currency.nominal = 100;
        currency.name = "currency_1";
        currency.value = "10";
        return currency;
    }

    public static Currency getTestCurrency2() {
        final Currency currency = new Currency();
        currency.charCode = "c2";
        currency.numCode = 2;
        currency.nominal = 100;
        currency.name = "currency_2";
        currency.value = "20";
        return currency;
    }

    public static Currency getTestCurrency3() {
        final Currency currency = new Currency();
        currency.charCode = "c3";
        currency.numCode = 3;
        currency.nominal = 100;
        currency.name = "currency_3";
        currency.value = "30";
        return currency;
    }

    public static List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(getTestCurrency1());
        currencies.add(getTestCurrency2());
        currencies.add(getTestCurrency3());
        return currencies;
    }

    public static CurrencyApiResponse getCurrencyApiResponse() {
        CurrencyApiResponse response = new CurrencyApiResponse();
        response.currencies = getCurrencies();
        return response;
    }

    public static String getCurrenciesXmlString() {
        return "<ValCurs>\n" +
                "   <Valute>\n" +
                "      <NumCode>1</NumCode>\n" +
                "      <CharCode>c1</CharCode>\n" +
                "      <Nominal>100</Nominal>\n" +
                "      <Name>currency_1</Name>\n" +
                "      <Value>111</Value>\n" +
                "   </Valute>\n" +
                "   <Valute>\n" +
                "      <NumCode>2</NumCode>\n" +
                "      <CharCode>c2</CharCode>\n" +
                "      <Nominal>100</Nominal>\n" +
                "      <Name>currency_2</Name>\n" +
                "      <Value>222</Value>\n" +
                "   </Valute>\n" +
                "   <Valute>\n" +
                "      <NumCode>3</NumCode>\n" +
                "      <CharCode>c3</CharCode>\n" +
                "      <Nominal>100</Nominal>\n" +
                "      <Name>currency_3</Name>\n" +
                "      <Value>333</Value>\n" +
                "   </Valute>\n" +
                "</ValCurs>";
    }
}
