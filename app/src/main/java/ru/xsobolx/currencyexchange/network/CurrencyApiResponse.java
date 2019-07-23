package ru.xsobolx.currencyexchange.network;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Objects;

import ru.xsobolx.currencyexchange.domain.model.Currency;

@Root(name = "ValCurs")
public class CurrencyApiResponse {
    @ElementList(inline = true, name = "Valute")
    public List<Currency> currencies;

    public List<Currency> getCurrencies() {
        return currencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyApiResponse that = (CurrencyApiResponse) o;
        return Objects.equals(currencies, that.currencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencies);
    }
}
