package com.sora.zero.tgfc.view.threadContent;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.databinding.FragmentViewPageBinding;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsy on 4/1/17.
 */

public class ThreadContentPageFragment extends BaseFragment implements ThreadContentContract.View {
    private static final String ARG_PAGE = "page";
    private static final String CURRENT_PAGE = "current page";

    private ThreadContentFragment parentFragment;
    private FragmentViewPageBinding mBinding;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ThreadContentRecyclerAdapter mRecyclerAdapter;
    private ThreadContentContract.Presenter mPresenter;
    private int page;

    @Override
    public void onAttach(Context context) {
        this.LOG_TAG = "ThreadContentPageFragment";
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState == null){
            Bundle bundle = getArguments();
            this.page = bundle.getInt(ARG_PAGE);
        } else {
            this.page = savedInstanceState.getInt(CURRENT_PAGE);
        }


        this.LOG_TAG = this.LOG_TAG + page;
        L.d(LOG_TAG, "onCreateView");

        this.parentFragment = (ThreadContentFragment) this.getParentFragment();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_page ,container, false);

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mRecyclerView = mBinding.recyclerView;
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout;

        mRecyclerAdapter = new ThreadContentRecyclerAdapter(new ArrayList<>());

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadPosts(page));

    }

    @Override
    public void onResume(){
        super.onResume();
        if(mPresenter != null)
            mPresenter.subscribe(page);
    }

    public static ThreadContentPageFragment newInstance(int page){
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);

        ThreadContentPageFragment fragment = new ThreadContentPageFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe(page, false);
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribe(page, true);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_PAGE, this.page);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(@NonNull ThreadContentContract.Presenter presenter) {
        mPresenter = presenter;
    }
    @Override
    public void setLoadingIndicator(@NonNull boolean active){
        L.d("page" + page + " :set loading indicator: " + active);
        if(mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showPosts(@NonNull List<ThreadContentItem> posts) {
        if(mRecyclerAdapter != null)
            mRecyclerAdapter.replaceData(posts);

    }

    @Override
    public void showEmptyPosts() {
        mRecyclerAdapter.replaceData(new ArrayList<>());
    }

    @Override
    public void showLoadingPostsError() {
        showMessage(getString(R.string.loading_posts));
    }

    @Override
    public void showMessage(@NonNull String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }





}
