package com.sora.zero.tgfc.data.api.model;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;


/**
 * Created by zsy on 4/23/17.
 */

public final class ThreadType {
    private String typeId;
    private String typeName;

    public ThreadType(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

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
}
