package ru.xsobolx.currencyexchange.util;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class IoExecutor implements Executor {
    private static IoExecutor INSTANCE = null;

    private final static int POOL_SIZE = 2;

    private final ThreadPoolExecutor threadPoolExecutor;

    private IoExecutor(@NonNull ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public static IoExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IoExecutor((ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE));
        }
        return INSTANCE;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }
}
