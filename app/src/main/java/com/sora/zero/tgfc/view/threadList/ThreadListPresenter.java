package com.sora.zero.tgfc.view.threadList;


import com.sora.zero.tgfc.data.api.TGFCService;
import com.sora.zero.tgfc.data.api.model.wrapper.ForumThreadListWrapper;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.utils.PreferenceUtils;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsy on 3/30/17.
 */

public class ThreadListPresenter implements ThreadListContract.Presenter {

    private TGFCService mTGFCService;

    private ThreadListContract.View mThreadListView;

    private int mForumId;

    private int mPage;

    private CompositeDisposable mCompositeDisposable;

    @Inject
    ThreadListPresenter(TGFCService tgfcService, ThreadListContract.View view){
        mTGFCService = tgfcService;
        mThreadListView = view;

        mCompositeDisposable = new CompositeDisposable();
        mPage = 1;
        mForumId = PreferenceUtils.getLastViewedForumId();

    }

    @Inject
    void setupListeners() {
        mThreadListView.setPresenter(this);
    }

    @Override
    public void subscribe(){
        if(mThreadListView.isThreadListEmpty())
            loadThreads();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void loadThreads(){
        if(mForumId == 0)
            loadThreads(PreferenceUtils.getLastViewedForumId());
        else
            loadThreads(mForumId);

    }

    @Override
    public void loadMoreThreads() {
        if(mForumId == 0)
            loadMoreThreads(PreferenceUtils.getLastViewedForumId());
        else
            loadMoreThreads(mForumId);
    }

    public void loadMoreThreads(int forumId) {

        Disposable mDisposable = mTGFCService
                .getForumThreads(String.valueOf(forumId), String.valueOf(++mPage), "30")
                .map(ForumThreadListWrapper::fromSource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        wrapper -> {
                            if (wrapper.hasError)
                                mThreadListView.showMessage(wrapper.errorInfo);
                            else
                                mThreadListView.showMoreThreads(wrapper.getThreads()); },
                        // onError
                        throwable -> L.d(throwable.toString()),
                        // onComplete
                        () -> {});

        mCompositeDisposable.add(mDisposable);

    }

    public void loadThreads(int forumId) {
        mThreadListView.setLoadingIndicator(true);

        Disposable mDisposable = mTGFCService
                .getForumThreads(String.valueOf(forumId), String.valueOf(mPage),
                        String.valueOf(PreferenceUtils.getThreadNumPerForum()))
                .map(ForumThreadListWrapper::fromSource)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        wrapper -> {
                            if (wrapper.hasError)
                                mThreadListView.showMessage(wrapper.errorInfo);
                            else
                                mThreadListView.showThreads(wrapper.getThreads()); },
                        // onError
                        throwable -> L.d(throwable.toString()),
                        // onComplete
                        () -> mThreadListView.setLoadingIndicator(false));

        mCompositeDisposable.add(mDisposable);

    }

    @Override
    public void setForumId(int id) {
        mForumId = id;
        mPage = 1;
        mThreadListView.showEmptyThreads();
        setTitle();
        loadThreads();
    }

    @Override
    public void refresh(){
        setForumId(mForumId);
    }

    @Override
    public void setTitle() {
        mThreadListView.showTitle(mForumId);
    }
}
