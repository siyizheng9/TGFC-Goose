package com.sora.zero.tgfc.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;


/**
 * Created by zsy on 4/23/17.
 */

public final class ThreadType implements Parcelable{
    private String typeId;
    private String typeName;

    public ThreadType(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public ThreadType(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        this.typeId = data[0];
        this.typeName = data[1];
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.typeId,
                this.typeName});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ThreadType>() {
        public ThreadType createFromParcel(Parcel in) {
            return new ThreadType(in);
        }

        public ThreadType[] newArray(int size) {
            return new ThreadType[size];
        }
    };

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return  false;

        ThreadType other = (ThreadType) otherObject;

        return Objects.equal(this.typeId, other.typeId) &&
                Objects.equal(this.typeName, other.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.typeName, this.typeId);
    }


    @Override
    public String toString() {
        return "ThreadType{" +
                "typeId='" + typeId + '\'' + "; typeName='" + typeName + '\'' +
                '}';
    }

}
