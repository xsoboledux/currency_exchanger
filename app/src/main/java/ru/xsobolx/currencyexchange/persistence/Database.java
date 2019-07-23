package ru.xsobolx.currencyexchange.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import ru.xsobolx.currencyexchange.persistence.model.CurrencyDBModel;

public class Database extends SQLiteOpenHelper {
    private static Database INSTANCE = null;

    private static final String DATABASE_NAME = "currencies.db";
    private static final int DATABASE_VERSION = 1;

    private Database(@NonNull final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Database getInstance(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Database(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CurrencyDBModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CurrencyDBModel.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
