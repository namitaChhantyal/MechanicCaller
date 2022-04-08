package com.codebee.tradethrust.di.module;

import android.content.Context;

import com.codebee.tradethrust.di.ActivityScope;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by csangharsha on 1/26/18.
 */
@Module(includes = {
        ContextModule.class,
        NetworkModule.class
})
public class PicassoModule {

    @Provides
    @ActivityScope
    public Picasso providesPicasso(Context context, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();
    }

    @Provides
    @ActivityScope
    public OkHttp3Downloader providesOkHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

}
