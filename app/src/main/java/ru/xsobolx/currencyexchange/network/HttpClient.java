package ru.xsobolx.currencyexchange.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static HttpClient INSTANCE = null;

    private static final String METHOD_GET = "GET";

    private static final int CONNECT_TIMEOUT = 10000;

    private static final int READ_TIMEOUT = 10000;

    public static HttpClient getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClient();
                }
            }
        }
        return INSTANCE;
    }

    @Nullable
    public String get(@NonNull final String path) {
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder readStringBuilder = new StringBuilder();
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD_GET);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            int httpCode = connection.getResponseCode();
            if (httpCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    readStringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                String response = readStringBuilder.toString();
                return response;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
