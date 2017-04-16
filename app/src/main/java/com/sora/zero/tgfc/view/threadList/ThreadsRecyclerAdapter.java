package com.sora.zero.tgfc.view.threadList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.databinding.ItemLoaderLayoutBinding;
import com.sora.zero.tgfc.databinding.ItemThreadListBinding;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zsy on 3/30/17.
 */

public class ThreadsRecyclerAdapter extends
RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ForumThread> mThreads;
    private ThreadListContract.Presenter mPresenter;
    //see http://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener-and-how-recyclerview-is-dif
    private final PublishSubject<ForumThread> onClickSubject = PublishSubject.create();

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

    public void replaceData(@NonNull List<ForumThread> threads){
        setList(threads);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<ForumThread> threads) {
        addList(threads);
        notifyDataSetChanged();
    }

    private void setList(@NonNull List<ForumThread> threads){
        mThreads = threads;
    }

    private void addList(@NonNull List<ForumThread> newThreads) {
        int size = this.mThreads.size();
        for (int i = 0; i < newThreads.size(); i++) {
            boolean hasThread = false;
            for (int j = 0; j < size; j++) {
                if(this.mThreads.get(j).getTid() == newThreads.get(i).getTid()) {
                    hasThread = true;
                    break;
                }
            }
            if (hasThread == false) {
                this.mThreads.add(newThreads.get(i));
            }
        }
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemThreadListBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = ItemThreadListBinding.bind(rowView);
        }

        public ItemThreadListBinding getBinding() {
            return binding;
        }
    }

    public static class LoaderViewHolder extends RecyclerView.ViewHolder {
        private ItemLoaderLayoutBinding binding;

        public LoaderViewHolder(View rowView) {
            super(rowView);
            binding = ItemLoaderLayoutBinding.bind(rowView);
        }

        public ItemLoaderLayoutBinding getBinding() {
            return binding;
        }

    }

    public ThreadsRecyclerAdapter(List<ForumThread> recyclerThreads, ThreadListContract.Presenter presenter) {
        this.mThreads = recyclerThreads;
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == VIEWTYPE_LOADER) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loader_layout, parent, false);
            return new LoaderViewHolder(v);

        } else {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_thread_list, parent, false);
            BindingHolder holder = new BindingHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if( holder instanceof LoaderViewHolder) {
            mPresenter.loadMoreThreads();
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) holder;
            return;
        }
        final ForumThread thread = mThreads.get(position);
        BindingHolder bindingHolder = (BindingHolder) holder;
        bindingHolder.getBinding().getRoot().setOnClickListener(v -> onClickSubject.onNext(thread));
        bindingHolder.getBinding().setForumThread(thread);
        bindingHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mThreads.size();
    }

    @Override
    public int getItemViewType(int position) {
        // loader can't be at position 0
        // loader can only be at the last position
        if(position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;
    }

    public Observable<ForumThread> getPositionClicks() {
        return onClickSubject;
    }
}
