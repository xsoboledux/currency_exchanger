package ru.xsobolx.currencyexchange.network;

import androidx.annotation.NonNull;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class XmlParser {
    private static XmlParser INSTANCE = null;

    private XmlParser() {

    }

    public static XmlParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XmlParser();
        }
        return INSTANCE;
    }

    public <T> T fromXML(@NonNull final String xml, @NonNull final Class<? extends T> classOfT) throws Exception {
        final Reader reader = new StringReader(xml);
        final Persister persister = new Persister();
        return persister.read(classOfT, reader, false);
    }

    public String toXml(@NonNull final Object src) throws Exception {
        final Writer writer = new StringWriter();
        final Serializer serializer = new Persister();
        serializer.write(src, writer);
        return writer.toString();
    }
}
