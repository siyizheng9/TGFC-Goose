package com.sora.zero.tgfc.widget.Okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zsy on 4/25/17.
 */

public class RewriteCharsetInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        if (request.url().host().contains("club.tgfcer.com")){
            Response response = chain.proceed(chain.request());
            return  response.newBuilder()
                    .header("Content-Type", "text/html; charset=gbk")
                    .build();
        }
        else {
            return chain.proceed(chain.request());
        }

    }
}
