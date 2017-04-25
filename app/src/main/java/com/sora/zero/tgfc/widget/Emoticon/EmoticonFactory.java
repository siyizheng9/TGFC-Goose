package com.sora.zero.tgfc.widget.Emoticon;

import android.content.Context;
import android.util.SparseArray;

import com.google.common.collect.ImmutableList;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.Emoticon;

import java.util.List;


/**
 * Forked from ykrank
 * A factory provides emotions.
 */

public class EmoticonFactory {

    public static final String ASSET_PATH_EMOTICON = "file:///android_asset/image/emoticon/";

    private final List<String> mEmoticonTypeTitles;
    private final SparseArray<List<Emoticon>> mEmoticons;

    public EmoticonFactory(Context context) {
        mEmoticonTypeTitles = ImmutableList.copyOf(context.getResources().getStringArray(
                R.array.emoticon_type));
        mEmoticons = new SparseArray<>();
    }

    public List<String> getEmoticonTypeTitles() {
        return mEmoticonTypeTitles;
    }

    public List<Emoticon> getEmoticonsByIndex(int index) {
        List<Emoticon> emoticons = mEmoticons.get(index);
        if(emoticons == null) {
            switch (index) {
                case 0:
                    emoticons = getDefaultEmoticonList();
                    break;
                default:
                    throw new IllegalStateException("Unknown emoticon index: " + index + ".");
            }
        }
        mEmoticons.put(index, emoticons);

        return emoticons;
    }

    private List<Emoticon> getDefaultEmoticonList() {
        ImmutableList.Builder<Emoticon> builder = ImmutableList.builder();
        add(builder, "default/er.gif", ":er: ");
        add(builder, "default/r1.gif", ":r1: ");
        add(builder, "default/r2.gif", ":r2: ");
        add(builder, "default/r3.gif", ":r3: ");
        add(builder, "default/r4.gif", ":r4: ");
        add(builder, "default/r5.gif", ":r5: ");
        add(builder, "default/r6.gif", ":r6: ");
        add(builder, "default/r7.gif", ":r7: ");
        add(builder, "default/r8.gif", ":r8: ");
        add(builder, "default/r9.gif", ":r9: ");
        add(builder, "default/r10.gif", ":r10: ");
        return builder.build();
    }

    private void add(ImmutableList.Builder<Emoticon> builder, String emoticonFileName, String emoticonEntity){
        builder.add(new Emoticon(emoticonFileName, emoticonEntity));
    }

}
