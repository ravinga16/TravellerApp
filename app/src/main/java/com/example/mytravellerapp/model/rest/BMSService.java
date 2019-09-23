package com.example.mytravellerapp.model.rest;

import com.example.mytravellerapp.BuildConfig;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.model.rest.exceptions.RxErrorHandlingCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BMSService {

    private BMSAPI mBMSAPI;

    public BMSService(){
        String url = DomainConstants.SERVER_URL;
//        Retrofit restAdapter = new Retrofit
//                .Builder()
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(url)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();

        //retrofit library itself create and implement the interface which call for API
        mBMSAPI = restAdapter.create(BMSAPI.class);
    }


    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // Log Requests and Responses
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(logging);
        }

        return client.build();
    }

    public BMSAPI getApi() {
        return mBMSAPI;
    }
}
