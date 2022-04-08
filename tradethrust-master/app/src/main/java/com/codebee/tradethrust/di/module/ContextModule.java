package com.codebee.tradethrust.di.module;

import android.content.Context;

import com.codebee.tradethrust.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by csangharsha on 1/26/18.
 */
@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ActivityScope
    public Context providesContext() {
        return context;
    }

}
