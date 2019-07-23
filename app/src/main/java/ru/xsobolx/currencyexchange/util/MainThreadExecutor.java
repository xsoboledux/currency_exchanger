package ru.xsobolx.currencyexchange.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

public class MainThreadExecutor implements Executor {
    private static MainThreadExecutor INSTANCE = null;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private MainThreadExecutor() {
    }

    public static MainThreadExecutor getInstance() {
        if (INSTANCE == null) {
            synchronized (MainThreadExecutor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainThreadExecutor();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        mainThreadHandler.post(runnable);
    }
}
