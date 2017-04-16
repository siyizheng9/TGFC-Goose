package com.sora.zero.tgfc.data.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zsy on 3/18/17.
 */

public interface TGFCService {

    /**
     * @param forumId forum ID
     * @param page forum page
     * @param tp number of post to display
     */
    @GET("index.php?action=forum")
    Observable<String> getForumThreads(@Query("fid") String forumId, @Query("page") String page,
            @Query("pp") String tp);

    @GET("index.php?action=thread&pic=1")
    Observable<String> getThreadContent(@Query("tid") String forumId, @Query("page") String page,
            @Query("pp") String tp);

    @GET("index.php?action=my")
    Observable<String> getUserInfo(@Query("uid") String uid);

    @GET("index.php?action=my")
    Observable<String> getAccountInfo();

    @FormUrlEncoded
    @POST(APIURL.WAP_LOGIN_URL)
    Observable<String> login(@Field("username") String username, @Field("password") String password);
}
