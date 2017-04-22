package com.sora.zero.tgfc.data.event;

/**
 * Created by zsy on 4/22/17.
 */

public final class EmoticonClickEvent {

    private final String emoticonEntity;

    public EmoticonClickEvent(String emoticonEntity) {
        this.emoticonEntity = emoticonEntity;
    }

    public String getEmoticonEntity() {
        return emoticonEntity;
    }
}
