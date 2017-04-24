package com.sora.zero.tgfc.data.api;

/**
 * Created by zsy on 3/18/17.
 */

/**
 * forked from https://github.com/cfan8/TGFC
 */
public class APIURL {
    public static final String DOMAIN_NAME = "tgfcer.com";
    public static final String WAP_DOMAIN_NAME = "wap." + DOMAIN_NAME;
    public static final String WAP_BASE_URL = "https://" + WAP_DOMAIN_NAME;
    public static final String WAP_API_URL = WAP_BASE_URL + "/index.php";
    public static final String WAP_VIEW_FORUM_URL = WAP_API_URL + "?action=forum&fid=";
    public static final String WAP_VIEW_CONTENT_URL = WAP_API_URL + "?action=thread&sc=1&tid=";
    public static final String WAP_LOGIN_URL = WAP_API_URL + "?action=login";
    public static final String WAP_LOGOUT_URL = WAP_API_URL + "?action=login&logout=yes";
    public static final String WAP_MY_INFO = WAP_API_URL + "?action=my";
    public static final String WAP_POST_NEW = WAP_API_URL + "?action=post&do=newthread";
    public static final String WAP_POST_REPLY = WAP_API_URL + "?action=post&do=reply";
    public static final String WAP_POST_EDIT = WAP_API_URL + "?action=post&do=edit";
    public static final String DEFAULT_AVATAR_URL = "http://club.tgfcer.com/images/avatars/noavatar.gif";

    public static final String WEB_CUSTOM_AVATARS = "http://club." + DOMAIN_NAME + "/customavatars/";

    public static final String WEB_FORUM = "http://club.tgfcer.com/forumdisplay.php?fid=";

    public static final String ANDROID_CLIENT_SIGNATURE_REGEX = "<br(?:\\s*?\\/)?>\\s*_{8,}\\s*<br(?:\\s*?\\/)?>\\s*(发送自.+?客户端)";

    public static String getAvatarUrl(int uid) {
        return WEB_CUSTOM_AVATARS + uid + ".jpg";
    }
}
