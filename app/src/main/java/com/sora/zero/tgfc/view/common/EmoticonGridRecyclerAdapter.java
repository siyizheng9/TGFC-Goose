package com.sora.zero.tgfc.view.common;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.Emoticon;
import com.sora.zero.tgfc.databinding.ItemEmoticonBinding;
import com.sora.zero.tgfc.viewmodel.EmoticonViewModel;
import com.sora.zero.tgfc.widget.EventBus;

import java.util.List;

/**
 * Created by zsy on 4/22/17.
 */

public class EmoticonGridRecyclerAdapter
        extends RecyclerView.Adapter<EmoticonGridRecyclerAdapter.BindingViewHolder> {

    private final LayoutInflater mLayoutInflater;

    private final List<Emoticon> mEmoticons;
    private final DrawableRequestBuilder<Uri> mEmoticonRequestBuilder;

    private final EventBus mEventBus;

    public EmoticonGridRecyclerAdapter(Activity activity, List<Emoticon> emoticons) {
        mLayoutInflater = activity.getLayoutInflater();
        this.mEmoticons = emoticons;
        mEmoticonRequestBuilder = Glide.with(activity).from(Uri.class);
        mEventBus = App.getAppComponent().getEventBus();

        setHasStableIds(true);
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmoticonBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                R.layout.item_emoticon, parent, false);
        binding.setEventBus(mEventBus);
        binding.setDrawableRequestBuilder(mEmoticonRequestBuilder);
        binding.setEmoticonViewModel(new EmoticonViewModel());

        return new BindingViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        holder.itemEmoticonBinding.getEmoticonViewModel().emoticon.set(mEmoticons.get(position));
        holder.itemEmoticonBinding.executePendingBindings();
    }

    @Override
    public void onViewDetachedFromWindow(BindingViewHolder holder) {
        Glide.clear(holder.itemEmoticonBinding.image);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mEmoticons.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static final class BindingViewHolder extends RecyclerView.ViewHolder{

        private final ItemEmoticonBinding itemEmoticonBinding;

        public BindingViewHolder(ItemEmoticonBinding itemEmoticonBinding) {
            super(itemEmoticonBinding.getRoot());

            this.itemEmoticonBinding = itemEmoticonBinding;
        }
    }
}
