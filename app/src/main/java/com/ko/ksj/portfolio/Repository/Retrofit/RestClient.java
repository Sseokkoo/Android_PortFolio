package com.ko.ksj.portfolio.Repository.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {
    private static Retrofit retrofit = null;
    private static RetroInterface RETRO_INTERFACE = null;

    public static String servermUrl = "";

    public static Retrofit initRestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        String version = "1.0";

        // 실 서버
//        String mUrl = "http://www.livecareworld.com/LIVECARE/LC_INJECT/1.2/";
        // 테스트 서버
        String mUrl = "https://admin.livepatch.xyz/LC_API/" + version + "/";

        servermUrl = mUrl;

        retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getRestClient() {
        if (retrofit == null)
            initRestClient();

        return retrofit;
    }

    public static <S> void createService(Class<S> serviceClass) {
        RETRO_INTERFACE = (RetroInterface) getRestClient().create(serviceClass);
    }

    public static RetroInterface getService() {
        if (RETRO_INTERFACE == null)
            createService(RetroInterface.class);

        return RETRO_INTERFACE;
    }
}