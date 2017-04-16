package com.sora.zero.tgfc;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by zsy on 3/18/17.
 */

public final class App extends Application {

    private static App sApp;

    private AppComponent mAppComponent;

    public static App get() { return sApp; }

    public static AppComponent getAppComponent() {
        return sApp.mAppComponent;
    }

    public void onCreate() {
        super.onCreate();
        sApp = this;

        if(BuildConfig.DEBUG){
            Stetho.initializeWithDefaults(this);
        }

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
