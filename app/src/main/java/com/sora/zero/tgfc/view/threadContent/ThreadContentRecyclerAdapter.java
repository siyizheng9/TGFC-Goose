package com.sora.zero.tgfc.view.threadContent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.databinding.ItemPostBinding;
import com.sora.zero.tgfc.widget.EventBus;

import java.util.List;

/**
 * Created by zsy on 4/1/17.
 */

public class ThreadContentRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ThreadContentItem> mThreadContentItems;
    private EventBus mEventBus;

    public void replaceData(@NonNull List<ThreadContentItem> items){
        setList(items);
        notifyDataSetChanged();
    }

    private void setList(@NonNull List<ThreadContentItem> items){
        mThreadContentItems = items;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = ItemPostBinding.bind(rowView);
        }

        public ItemPostBinding getBinding() {
            return binding;
        }
    }

    public ThreadContentRecyclerAdapter(List<ThreadContentItem> items) {
        this.mThreadContentItems = items;
        this.mEventBus = App.getAppComponent().getEventBus();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ThreadContentItem item = mThreadContentItems.get(position);
        BindingHolder bindingHolder = (BindingHolder) holder;
        ItemPostBinding mBinding = bindingHolder.getBinding();
        mBinding.setThreadPost(item);
        mBinding.setEventBus(mEventBus);
        bindingHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mThreadContentItems.size();
    }

    public boolean isThreadPostListEmpty(){
        return this.mThreadContentItems.isEmpty();
    }




}
