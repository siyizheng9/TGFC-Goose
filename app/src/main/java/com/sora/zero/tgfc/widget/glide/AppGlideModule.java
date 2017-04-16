package com.sora.zero.tgfc.widget.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.sora.zero.tgfc.App;

import java.io.InputStream;

/**
 * Created by zsy on 4/6/17.
 */

/**
 * Forked from ykrank
 */
public class AppGlideModule implements GlideModule{

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register the OkHttp for Glide
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(
                App.getAppComponent().getOkHttpClient()));
    }
}
