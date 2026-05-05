package com.example.ap4.network;
import android.content.Context;

import com.example.ap4.session.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {

    private final Context context;

    public LogInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = TokenManager.getToken(context);
        Request originalRequest = chain.request();

        if (token == null || token.trim().isEmpty()) {
            return chain.proceed(originalRequest);
        }

        Request modifiedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(modifiedRequest);
    }
}