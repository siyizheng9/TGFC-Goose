package com.sora.zero.tgfc;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.sora.zero.tgfc.data.api.APIURL;
import com.sora.zero.tgfc.data.api.TGFCService;
import com.sora.zero.tgfc.data.api.model.User;
import com.sora.zero.tgfc.widget.EventBus;
import com.sora.zero.tgfc.widget.PersistentHttpCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zsy on 3/18/17.
 */

@Module
public class AppModule {

    private final App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    CookieManager providerCookieManager(Context context) {
        return new CookieManager(new PersistentHttpCookieStore(context), CookiePolicy.ACCEPT_ALL);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(CookieManager cookieManager){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(17, TimeUnit.SECONDS);
        builder.writeTimeout(17, TimeUnit.SECONDS);
        builder.readTimeout(77, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        if(BuildConfig.DEBUG){
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();

    }

    @Provides
    @Singleton
    TGFCService provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(APIURL.WAP_BASE_URL+"/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TGFCService.class);

    }

    @Provides
    @Singleton
    User provideUser() {
        return new User();
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return new EventBus();
    }

}
