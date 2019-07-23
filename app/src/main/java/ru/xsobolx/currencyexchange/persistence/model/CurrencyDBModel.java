package ru.xsobolx.currencyexchange.persistence.model;

import java.util.Objects;

public class CurrencyDBModel {
    public static final String TABLE_NAME = "currency";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NUM_CODE = "num_code";
    public static final String COLUMN_CHAR_CODE = "char_code";
    public static final String COLUMN_NOMINAL = "nominal";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VALUE = "column_value";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NUM_CODE + " INTEGER, "
                    + COLUMN_CHAR_CODE + " TEXT, "
                    + COLUMN_NOMINAL + " INTEGER, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_VALUE + " REAL "
                    + ")";

    private final int id;
    private final int numId;
    private final String charCode;
    private final int nominal;
    private final String name;
    private final String value;

    public CurrencyDBModel(int id, int numCode, String charCode, int nominal, String name, String value) {
        this.id = id;
        this.numId = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getNominal() {
        return nominal;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNumId() {
        return numId;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyDBModel that = (CurrencyDBModel) o;
        return id == that.id &&
                numId == that.numId &&
                nominal == that.nominal &&
                Objects.equals(charCode, that.charCode) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numId, charCode, nominal, name, value);
    }
}
