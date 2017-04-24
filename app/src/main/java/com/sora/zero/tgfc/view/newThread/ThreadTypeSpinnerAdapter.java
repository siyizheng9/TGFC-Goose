package com.sora.zero.tgfc.view.newThread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ThreadType;

import java.util.List;

/**
 * Created by zsy on 4/24/17.
 */

public class ThreadTypeSpinnerAdapter extends ArrayAdapter<ThreadType> {

    public ThreadTypeSpinnerAdapter(Context context, List<ThreadType> types) {
        super(context, 0, types);
    }

    @Override
    public View getView(int position, View converterView, ViewGroup parent){
        ThreadType type = getItem(position);

        if( converterView == null){
            converterView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_thread_type, parent, false);
        }
        ((TextView) converterView).setText(type.getTypeName());

        return converterView;
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(getItem(position).getTypeId());
    }
}
