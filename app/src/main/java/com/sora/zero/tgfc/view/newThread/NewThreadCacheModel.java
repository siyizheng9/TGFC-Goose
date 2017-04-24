package com.sora.zero.tgfc.view.newThread;

import com.google.common.base.Objects;

/**
 * A model for caching user input when configuration change happens
 */

public class NewThreadCacheModel {
    // store thread type selection
    private int selectPosition;
    private String title;
    private String message;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewThreadCacheModel)) return false;
        NewThreadCacheModel Other = (NewThreadCacheModel) o;
        return selectPosition == Other.selectPosition &&
                Objects.equal(title, Other.title) &&
                Objects.equal(message, Other.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(selectPosition, title, message);
    }
}
