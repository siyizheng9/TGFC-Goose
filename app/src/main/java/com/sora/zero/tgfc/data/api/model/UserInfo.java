package com.sora.zero.tgfc.data.api.model;

import com.google.common.base.Optional;
import com.sora.zero.tgfc.utils.L;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsy on 4/7/17.
 */

public class UserInfo {

    private String userName;
    private String avatarUrl;
    private String uid;

    public static Optional<UserInfo> fromSource(String source) {
        source = StringEscapeUtils.unescapeHtml4(source);

        Pattern pattern = Pattern.compile("<b>(.*)<\\/b>.*<br \\/><img src=\"([^=]*)\" .*UID: (\\d*)");
        Matcher matcher = pattern.matcher(source);

        if(matcher.find()){
            UserInfo userInfo = new UserInfo();
            userInfo.userName = matcher.group(1);
            userInfo.avatarUrl = matcher.group(2);
            userInfo.uid = matcher.group(3);

           // L.d(userInfo.toString());

            return Optional.of(userInfo);
        } else {
            return Optional.absent();
        }

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(uid, avatarUrl, userName);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' + "; uid='" + uid + '\'' +
                "; avatarUrl='" + avatarUrl + '}';
    }
}
