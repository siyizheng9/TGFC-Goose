package com.sora.zero.tgfc.data.api.model;

/**
 * Created by zsy on 3/22/17.
 */

import android.support.annotation.Nullable;

/**
 * this class represents a post or a reply in a thread
 */
public class ThreadContentItem {

    private String posterName;
    private String postTime;
    private int floorNum;
    private int posterUID;
    private int pid;
    @Nullable private String quotedInfo;
    @Nullable private String quotedText;
    private String mainText;
    private int ratings;
    private String platformInfo;
    private boolean canEdit;
    private boolean isMainPost;

    @Override
    public String toString() {
        return "ThreadContentItem{" +
                "posterName='" + posterName + '\'' + "; postTime='" + postTime + '\'' +
                "; floorNum='" + floorNum + '\'' + "; posterUID='" + posterUID + '\'' +
                ": pid='" + pid + '\'' + "; quotedInfo='" +
                quotedInfo + '\'' + "; quotedText='" + quotedText + '\'' +
                ": mainText='" + mainText + '\'' + "; ratings='" +
                ratings + '\'' + "; platformInfo='" + platformInfo + '\'' +
                ": canEdit='" + canEdit + '\'' + "; isMainPost='" + isMainPost +
                '}';
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(posterName, postTime, floorNum,
                posterUID, pid, quotedInfo, quotedText, mainText, ratings, platformInfo,
                canEdit, isMainPost);
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(int posterUID) {
        this.posterUID = posterUID;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Nullable
    public String getQuotedInfo() {
        return quotedInfo;
    }

    public void setQuotedInfo(String quotedInfo) {
        this.quotedInfo = quotedInfo;
    }

    @Nullable
    public String getQuotedText() {
        return quotedText;
    }

    public void setQuotedText(String quotedText) {
        this.quotedText = quotedText;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getPlatformInfo() {
        return platformInfo;
    }

    public void setPlatformInfo(String platformInfo) {
        this.platformInfo = platformInfo;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isMainPost() {
        return isMainPost;
    }

    public void setMainPost(boolean mainPost) {
        isMainPost = mainPost;
    }
}
