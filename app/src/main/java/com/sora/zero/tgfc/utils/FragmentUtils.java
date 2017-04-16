package com.sora.zero.tgfc.utils;


import static com.google.common.base.Preconditions.checkNotNull;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.api.model.ForumThread;
import com.sora.zero.tgfc.data.api.model.ThreadContentItem;
import com.sora.zero.tgfc.view.threadContent.ThreadContentFragment;
import com.sora.zero.tgfc.view.threadList.ThreadListContract;
import com.sora.zero.tgfc.view.threadList.ThreadListFragment;

/**
 * Created by zsy on 3/30/17.
 */

public class FragmentUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    public static void showFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.flContent, fragment)
                .addToBackStack(ThreadListFragment.TRANSACTION_TAG)
                .commit();
    }

    public static void showForum(@NonNull FragmentManager fragmentManager,
            ThreadListContract.Presenter presenter, int fid) {
        checkNotNull(fragmentManager);

        int count = fragmentManager.getBackStackEntryCount();
        for(int i = 0 ; i < count; i++) {

            String transacName = fragmentManager.getBackStackEntryAt(0).getName();
            L.d("transacName " + i + " " + transacName);
        }
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(ThreadListFragment.TRANSACTION_TAG, 1);
        //boolean fragmentPopped = fragmentManager.popBackStackImmediate();

        L.d("fragment popped: " + fragmentPopped + " BackStackEntryCount: " + count);

        presenter.setForumId(fid);

    }

    public static void showThread(@NonNull FragmentManager fragmentManager, @NonNull
            ForumThread forumThread) {
        ThreadContentFragment fragment =
                ThreadContentFragment.newInstance(forumThread.getTid(), forumThread.getTitle());

        showFragment(fragmentManager,fragment);


    }

}
