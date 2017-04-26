package com.sora.zero.tgfc.viewmodel;

import android.databinding.ObservableField;
import android.view.View;

import com.sora.zero.tgfc.data.api.model.Emoticon;
import com.sora.zero.tgfc.data.event.EmoticonClickEvent;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.widget.EventBus;

/**
 * Created by zsy on 4/22/17.
 */

public class EmoticonViewModel {

    public final ObservableField<Emoticon> emoticon = new ObservableField<>();

    public View.OnClickListener clickEmotion(EventBus eventBus) {
        return v -> {
            // notify ReplyFragment that emoticon had been clicked
            L.d("EmoticonViewModel" + emoticon.get().getEntity());
            eventBus.post(new EmoticonClickEvent(emoticon.get().getEntity()));
        };
    }
}
