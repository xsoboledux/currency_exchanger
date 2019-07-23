package ru.xsobolx.currencyexchange.network;

import org.junit.Before;
import org.junit.Test;

import ru.xsobolx.currencyexchange.test_data.TestCurrencies;

import static org.junit.Assert.assertEquals;

public class XmlParserTest {
    private XmlParser xmlParser;

    @Before
    public void setUp() {
        xmlParser = XmlParser.getInstance();
    }

    @Test
    public void testCurrenciesApiResponseSerializing() throws Exception {
        CurrencyApiResponse responseToSerialize = TestCurrencies.getCurrencyApiResponse();
        String serializedString = xmlParser.toXml(responseToSerialize);

        String expected = TestCurrencies.getCurrenciesXmlString();

        assertEquals(expected, serializedString);
    }

    @Test
    public void testCurrenciesApiResponseDeserializing() throws Exception {
        String xmlStringToDeserialize = TestCurrencies.getCurrenciesXmlString();
        CurrencyApiResponse deserializedResponse = xmlParser.fromXML(xmlStringToDeserialize, CurrencyApiResponse.class);

        CurrencyApiResponse expected = TestCurrencies.getCurrencyApiResponse();

        assertEquals(expected, deserializedResponse);
    }
}