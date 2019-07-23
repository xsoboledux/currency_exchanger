package ru.xsobolx.currencyexchange.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.xsobolx.currencyexchange.domain.model.Currency;
import ru.xsobolx.currencyexchange.persistence.model.CurrencyDBModel;

public class CurrencyDAO {
    private static CurrencyDAO INSTANCE = null;

    private final Database database;

    private CurrencyDAO(@NonNull Database database) {
        this.database = database;
    }

    public static CurrencyDAO getInstance(@NonNull Database database) {
        if (INSTANCE == null) {
            synchronized (CurrencyDAO.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrencyDAO(database);
                }
            }
        }
        return INSTANCE;
    }

    public void saveCurrency(@NonNull final Currency currency) {
        final ContentValues values = new ContentValues();
        values.put(CurrencyDBModel.COLUMN_CHAR_CODE, currency.getCharCode());
        values.put(CurrencyDBModel.COLUMN_NAME, currency.getName());
        values.put(CurrencyDBModel.COLUMN_NOMINAL, currency.getNominal());
        values.put(CurrencyDBModel.COLUMN_NUM_CODE, currency.getNumCode());
        values.put(CurrencyDBModel.COLUMN_VALUE, currency.getValue());
        database.getWritableDatabase().insert(CurrencyDBModel.TABLE_NAME, null, values);
        database.close();
    }

    @NonNull
    public List<Currency> getCurrencies() {
        final String query = "SELECT * FROM " + CurrencyDBModel.TABLE_NAME;
        final Cursor cursor = database.getWritableDatabase().rawQuery(query, null);
        final List<Currency> currencies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Currency currency = new Currency();
            currency.numCode = cursor.getInt(cursor.getColumnIndex(CurrencyDBModel.COLUMN_NUM_CODE));
            currency.charCode = cursor.getString(cursor.getColumnIndex(CurrencyDBModel.COLUMN_CHAR_CODE));
            currency.nominal = cursor.getInt(cursor.getColumnIndex(CurrencyDBModel.COLUMN_NOMINAL));
            currency.name = cursor.getString(cursor.getColumnIndex(CurrencyDBModel.COLUMN_NAME));
            currency.value = cursor.getString(cursor.getColumnIndex(CurrencyDBModel.COLUMN_VALUE));
            currencies.add(currency);
        }
        cursor.close();
        database.close();
        return currencies;
    }

    public void deleteAll() {
        final String query = "DELETE FROM " + CurrencyDBModel.TABLE_NAME;
        database.getWritableDatabase().execSQL(query);
        database.close();
    }
}
