package com.sora.zero.tgfc.view.threadContent;

import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.view.base.BasePresenter;
import com.sora.zero.tgfc.view.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by zsy on 4/1/17.
 */

public interface ThreadContentContract {
    interface View extends BaseView<ThreadContentContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showPosts(List<ThreadContentItem> Posts);

        void showLoadingPostsError();

        void showMessage(String msg);

        void showEmptyPosts();

        boolean isActive();

        boolean isThreadPostListEmpty();

    }

    interface Presenter extends BasePresenter {

        void loadPosts(int page);

        void setupListeners(ThreadContentContract.View view, int page);

        void subscribe(int page);

        void unsubscribe(int page, boolean isOnDestroy);

        public Observable<Integer> getTotalPageUpdate();

    }
}
