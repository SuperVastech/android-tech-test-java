package com.bridge.androidtechnicaltest.di;

import com.bridge.androidtechnicaltest.network.PupilService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final String BASE_URL = "https://androidtechnicaltestapi-test.bridgeinternationalacademies.com/";
    private static final Integer API_TIMEOUT = 30;
    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(API_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(API_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(API_TIMEOUT, TimeUnit.SECONDS);

        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .build();
                return  chain.proceed(newRequest);
            }
        };
        builder.addInterceptor(requestInterceptor);

        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }


    @Provides
    @Singleton
    PupilService provideNetworkApi(Retrofit retrofit) {
        return retrofit.create(PupilService.class);
    }
}
