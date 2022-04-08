package com.codebee.tradethrust.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.codebee.tradethrust.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by csangharsha on 5/23/18.
 */

@Module(includes = {ContextModule.class})
public class PreferenceModule {

    private Context context;

    public PreferenceModule(Context context) {
        this.context = context;
    }

    @Provides
    @ActivityScope
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences("PrefName", Context.MODE_PRIVATE);
    }

}
