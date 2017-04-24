package com.sora.zero.tgfc.view.threadList;

import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.view.base.BasePresenter;
import com.sora.zero.tgfc.view.base.BaseView;

import java.util.List;

/**
 * Created by zsy on 3/30/17.
 */

public interface ThreadListContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showThreads(List<ForumThread> threads);

        void showMoreThreads(List<ForumThread> threads);

        void showEmptyThreads();

        void showLoadingThreadsError();

        void showMessage(String msg);

        void showTitle(int forumId);

        boolean isActive();

        boolean isThreadListEmpty();

    }

    interface Presenter extends BasePresenter {

        void loadThreads();

        void loadMoreThreads();

        void setTitle();

        void setForumId(int forumId);

        void refresh();

        int getForumId();

    }
}
