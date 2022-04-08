package com.codebee.tradethrust.di.module;

import android.content.Context;
import android.net.Network;

import com.codebee.tradethrust.di.ActivityScope;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by csangharsha on 1/26/18.
 */

@Module(includes = {ContextModule.class})
public class NetworkModule {


    @Provides
    @ActivityScope
    public HttpLoggingInterceptor providesHttpLoggingInterceptor() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.v(message);
            }
        });

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }

    @Provides
    @ActivityScope
    public Cache providesCache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000); //10MB
    }

    @Provides
    @ActivityScope
    public File providesFile(Context context) {

        File file = new File(context.getCacheDir(), "OkHttp_cache");
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    @Provides
    @ActivityScope
    public OkHttpClient providesOkHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }

    @ActivityScope
    @Provides
    public Gson provideGson(){
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @ActivityScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson) {
        String mBaseUrl = ThrustConstant.BASE_URL;

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}
