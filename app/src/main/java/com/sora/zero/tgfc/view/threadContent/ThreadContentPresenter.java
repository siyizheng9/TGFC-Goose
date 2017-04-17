package com.sora.zero.tgfc.view.threadContent;

import android.support.annotation.NonNull;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.data.api.TGFCService;
import com.sora.zero.tgfc.data.api.model.wrapper.ThreadContentWrapper;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zsy on 4/1/17.
 */

public class ThreadContentPresenter implements ThreadContentContract.Presenter {

    private int mTid;

    private int mTotalPage = 1;

    private TGFCService mTGFCService;

    private final PublishSubject<Integer> onTotalPageUpdateSubject = PublishSubject.create();

    private WeakHashMap<Integer, ThreadContentContract.View> mPageMap;

    private HashMap<Integer, Disposable> mDisposableMap;

    public ThreadContentPresenter(int tid){
        mTGFCService = App.getAppComponent().getTGFCService();
        mTid = tid;

        mPageMap = new WeakHashMap<>();
        mDisposableMap= new HashMap<>();
    }

    @Override
    public void setupListeners(@NonNull ThreadContentContract.View view, int page) {
        view.setPresenter(this);
        mPageMap.put(page, view);
    }

    @Override
    public void loadPosts(int page) {

        L.d("loadPosts");

        final ThreadContentContract.View mView = mPageMap.get(page);

        if(mView == null)
            return;

        L.d("Loading page: " + page);

        mView.setLoadingIndicator(true);

        Disposable disposable = mTGFCService
                .getThreadContent(String.valueOf(this.mTid), String.valueOf(page),
                        String.valueOf(PreferenceUtils.getPostNumPerThread()))
                .map(s -> ThreadContentWrapper.fromSource(s, mTid, page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // onNext
                        wrapper -> {
                            if (wrapper.hasError)
                                mView.showMessage(wrapper.errorInfo);
                            else {
                                mView.showPosts(wrapper.getReplyList());
                                onTotalPageUpdateSubject.onNext(wrapper.getTotalPageCount());
                            }
                        },
                        // onError
                        throwable -> L.d(throwable.toString()),
                        // onComplete
                        () -> mView.setLoadingIndicator(false));

        mDisposableMap.put(page, disposable);

    }



    @Override
    public void subscribe(int page){
        if(mPageMap.get(page).isThreadPostListEmpty())
            loadPosts(page);
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {}

    @Override
    public void unsubscribe(int page, boolean isOnDestroy){
        Disposable mDisposable = mDisposableMap.get(page);
        if(mDisposable != null)
            mDisposable.dispose();
        mDisposableMap.remove(page);

        if(isOnDestroy)
            mPageMap.remove(page);
    }

    @Override
    public Observable<Integer> getTotalPageUpdate() {
        return onTotalPageUpdateSubject;
    }


}
