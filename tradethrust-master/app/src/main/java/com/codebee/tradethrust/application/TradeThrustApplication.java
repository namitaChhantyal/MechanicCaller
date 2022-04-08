package com.codebee.tradethrust.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.codebee.tradethrust.di.component.DaggerTradeThrustApplicationComponent;
import com.codebee.tradethrust.di.component.TradeThrustApplicationComponent;
import com.codebee.tradethrust.di.module.ContextModule;
import com.codebee.tradethrust.di.module.NetworkModule;
import com.codebee.tradethrust.di.module.PreferenceModule;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by csangharsha on 5/12/18.
 */

public class TradeThrustApplication extends Application {

    private Picasso picasso;
    private SharedPreferences preferences;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        TradeThrustApplicationComponent component = DaggerTradeThrustApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .preferenceModule(new PreferenceModule(this))
                .networkModule(new NetworkModule())
                .build();

        picasso = component.getPicasso();

        preferences = component.sharedPreferences();

        okHttpClient = component.okHttpClient();

        gson = component.gson();

        retrofit = component.retrofit();

    }

    public Picasso getPicasso() {
        return picasso;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public OkHttpClient okHttpClient() {
        return okHttpClient;
    }

    public Gson gson() {
        return gson;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
