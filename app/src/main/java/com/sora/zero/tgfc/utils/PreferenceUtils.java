package com.sora.zero.tgfc.utils;

import com.sora.zero.tgfc.App;

/**
 * Created by zsy on 4/1/17.
 */

public class PreferenceUtils {
    private static int lastViewedForumId = 10;

    public static int getLastViewedForumId() {
        return lastViewedForumId;
    }

    public static int getThreadNumPerForum() { return 30; }

    public static int getPostNumPerThread() { return 30; }

    public static void setLastViewedForumId(int forumId) {
        lastViewedForumId =  forumId;
    }

    public static boolean isLogedIn() {
        return App.getAppComponent().getUser().isLogged();
    }
}
