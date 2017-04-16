package com.sora.zero.tgfc.data.api.model;

/**
 * Created by zsy on 3/18/17.
 */

/**
 * this class represents a thread in a thread list
 */
public class ForumThread {

    private String title;
    private Boolean topFlag;
    private String author;
    private String lastReplierName;
    private int replyCount;
    private int tid;
    private int readCount;

    public Boolean getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(Boolean topFlag) {
        this.topFlag = topFlag;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getLastReplierName() {
        return lastReplierName;
    }

    public void setLastReplierName(String lastReplierName) {
        this.lastReplierName = lastReplierName;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForumThread ft = (ForumThread) o;

        return title.equals(ft.title) &&
                author.equals(ft.author) &&
                topFlag.equals(ft.topFlag) &&
                readCount == ft.readCount;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(title, author, topFlag,
                readCount, replyCount, lastReplierName, tid);
    }

    @Override
    public String toString() {
        return "Thread{" +
                "title='" + title + '\'' + "; topFlag='" + topFlag + '\'' +
                "; author='" + author + '\'' + "; readCount='" + readCount + '\'' +
                ": replyCount='" + replyCount + '\'' + "; lastReplierName='" +
                lastReplierName + '\'' + "; tid='" + tid + '\'' +
                '}';
    }
}
