package com.sora.zero.tgfc.view.threadList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zsy on 3/30/17.
 */

@Module
public class ThreadListPresenterModule {

    private final ThreadListContract.View mView;

    public ThreadListPresenterModule(ThreadListContract.View view) {
        mView = view;
    }

    @Provides
    ThreadListContract.View provideThreadListContractView() {
        return mView;
    }
}
