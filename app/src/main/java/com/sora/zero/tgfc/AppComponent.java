package com.sora.zero.tgfc;

import android.content.Context;

import com.sora.zero.tgfc.data.api.TGFCService;
import com.sora.zero.tgfc.data.api.model.User;
import com.sora.zero.tgfc.widget.EventBus;

import java.net.CookieManager;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by zsy on 3/18/17.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    OkHttpClient getOkHttpClient();

    TGFCService getTGFCService();

    User getUser();

    CookieManager getCookieManager();

    EventBus getEventBus();

}
