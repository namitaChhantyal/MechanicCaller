package com.codebee.tradethrust.di.component;

import android.content.SharedPreferences;

import com.codebee.tradethrust.di.ActivityScope;
import com.codebee.tradethrust.di.module.NetworkModule;
import com.codebee.tradethrust.di.module.PicassoModule;
import com.codebee.tradethrust.di.module.PreferenceModule;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by csangharsha on 5/12/18.
 */
@ActivityScope
@Component(modules = {
        PicassoModule.class,
        PreferenceModule.class
})
public interface TradeThrustApplicationComponent {
    Picasso getPicasso();

    SharedPreferences sharedPreferences();

    Gson gson();

    OkHttpClient okHttpClient();

    Retrofit retrofit();
}
