package com.sora.zero.tgfc.view.threadList;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ForumList;
import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.databinding.FragmentThreadListBinding;
import com.sora.zero.tgfc.utils.FragmentUtils;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.utils.ScrollChildSwipeRefreshLayout;
import com.sora.zero.tgfc.view.MainActivity;
import com.sora.zero.tgfc.view.base.BaseFragment;
import com.sora.zero.tgfc.view.newThread.NewThreadFragment;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by zsy on 3/25/17.
 */

public class ThreadListFragment extends BaseFragment implements ThreadListContract.View {

    // used for identifying fragment transaction
    public static int FRAGMENT_COUNT = 0;
    public static final String TRANSACTION_TAG = "ThreadListFragment";

    private static final String ARG_FRAGMENT_TITLE = "fragment_title";

    private ThreadListContract.Presenter mPresenter;

    private ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;

    private ThreadsRecyclerAdapter mThreadsRecyclerAdapter;

    private FragmentThreadListBinding mBinding;

    private MenuItem mRefreshOption;

    private boolean isLoading;


    @Override
    public void onAttach(Context context) {
        this.LOG_TAG = "ThreadListFragment" + FRAGMENT_COUNT;
        super.onAttach(context);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        L.d(LOG_TAG + " onCreateView");

        if(savedInstanceState == null) {
            Bundle bundle = getArguments();

            this.fragmentTitle = bundle.getString(ARG_FRAGMENT_TITLE);
        } else {
            this.fragmentTitle = savedInstanceState.getString(ARG_FRAGMENT_TITLE);
        }

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_thread_list, parent, false);

        return mBinding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        L.d(LOG_TAG + " onViewCreated");

        RecyclerView rvThreadList = mBinding.rvThreadList;
        mSwipeRefreshLayout = mBinding.refreshLayout;

        mThreadsRecyclerAdapter = new ThreadsRecyclerAdapter(new ArrayList<>(0), mPresenter);

        rvThreadList.setAdapter(mThreadsRecyclerAdapter);
        rvThreadList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // set up progress indicator
        mSwipeRefreshLayout.setScrollUpChild(rvThreadList);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadThreads());

        mThreadsRecyclerAdapter.getPositionClicks().subscribe(
                //OnNext
                forumThread -> {
                    //showEmptyThreads();
                    FragmentUtils.showThread(getFragmentManager(), forumThread);
                }
        );


    }

    @Override
    public void onResume(){
        super.onResume();
        mPresenter.subscribe();
        mPresenter.setTitle();

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        L.d(LOG_TAG + "onHiddenChanged");
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_FRAGMENT_TITLE, this.fragmentTitle);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        L.d(LOG_TAG + " onCreateOptionsMenu");
        menu.clear();

        inflater.inflate(R.menu.thread_list, menu);
        mRefreshOption = menu.findItem(R.id.action_refresh);
        mRefreshOption.setEnabled(!isLoading);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d(LOG_TAG + " onOptionsItemSelected" + " itemId: " + item.getItemId());
        switch (item.getItemId()){
           case R.id.action_refresh:
               mPresenter.refresh();
               break;
            case R.id.action_new_thread:
                FragmentUtils.showFragment(getFragmentManager(),
                        NewThreadFragment.newInstance(mPresenter.getForumId()));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public ThreadListFragment() {
        L.d("ThreadListFragment constructor called");
        ThreadListFragment.FRAGMENT_COUNT++;

    }


    public static ThreadListFragment newInstance(String fragmentTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FRAGMENT_TITLE, fragmentTitle);
        ThreadListFragment fragment = new ThreadListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void setPresenter(@NonNull ThreadListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean active){
        mSwipeRefreshLayout.setRefreshing(active);
        isLoading = active;
        if (mRefreshOption != null)
            mRefreshOption.setEnabled(!active);
    }

    @Override
    public void showThreads(List<ForumThread> threads){
        mThreadsRecyclerAdapter.replaceData(threads);
        mBinding.listContainer.setVisibility(View.VISIBLE);
        mBinding.showError.setVisibility(View.GONE);
    }

    @Override
    public void showMoreThreads(@NonNull List<ForumThread> threads) {
        mThreadsRecyclerAdapter.addData(Preconditions.checkNotNull(threads));
    }

    @Override
    public void showEmptyThreads() {
        mThreadsRecyclerAdapter.replaceData(new ArrayList<>());
        mBinding.listContainer.setVisibility(View.VISIBLE);
        mBinding.showError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingThreadsError(){
       showMessage(getString(R.string.loading_threads_error));
    }

    @Override
    public void showMessage(String message) {
        mBinding.listContainer.setVisibility(View.GONE);
        mBinding.showError.setVisibility(View.VISIBLE);
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTitle(int forumId){
        String newTitle = ForumList.getForumByFid(forumId).name;
        this.fragmentTitle = newTitle;
        setActionBarTitle(newTitle);
    }

    @Override
    public boolean isThreadListEmpty() {
        return mThreadsRecyclerAdapter.isThreadListEmpty();
    }


}
