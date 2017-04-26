package com.sora.zero.tgfc.view.newThread;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;
import com.sora.zero.tgfc.data.api.model.ThreadType;

import java.util.List;

/**
 * A model for caching user input when configuration change happens
 */

public class NewThreadCacheModel implements Parcelable{
    // store thread type selection
    private int selectPosition;
    private String title;
    private String message;
    private List<ThreadType> threadTypes;

    public NewThreadCacheModel(int selectPosition, String title, String message, List<ThreadType> threadTypes) {
        this.selectPosition = selectPosition;
        this.title = title;
        this.message = message;
        this.threadTypes = threadTypes;
    }

    public NewThreadCacheModel(Parcel in) {
        String[] data = new String[2];

        this.selectPosition = in.readInt();
        in.readStringArray(data);
        this.title = data[0];
        this.message = data[1];
        in.readTypedList(threadTypes, ThreadType.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.selectPosition);
        dest.writeStringArray(new String[] {
                this.title,
                this.message });
        dest.writeTypedList(threadTypes);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<NewThreadCacheModel>() {
        public NewThreadCacheModel createFromParcel(Parcel in){
            return new NewThreadCacheModel(in);
        }

        public NewThreadCacheModel[] newArray(int size) {
            return new NewThreadCacheModel[size];
        }

    };


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

    public List<ThreadType> getThreadTypes() {
        return threadTypes;
    }

    public void setThreadTypes(List<ThreadType> threadTypes) {
        this.threadTypes = threadTypes;
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
