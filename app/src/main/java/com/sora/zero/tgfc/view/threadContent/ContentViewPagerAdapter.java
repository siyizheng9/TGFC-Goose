package com.sora.zero.tgfc.view.threadContent;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.sora.zero.tgfc.utils.L;

import io.reactivex.Observable;


/**
 * Created by zsy on 4/1/17.
 */

public class ContentViewPagerAdapter extends FragmentStatePagerAdapter {

    private int totalPage = 1;

    private int tid;

    private ThreadContentContract.Presenter mPresenter;

    public ContentViewPagerAdapter(@NonNull FragmentManager fragmentManager, int tid) {

        super(fragmentManager);
        Preconditions.checkNotNull(fragmentManager);
        this.tid = tid;

        mPresenter = new ThreadContentPresenter(tid);

        mPresenter.getTotalPageUpdate().subscribe(
                totalPage -> {
                    //onNext
                    if(this.totalPage != totalPage && totalPage != 0){
                        this.totalPage = totalPage;
                        this.notifyDataSetChanged();
                    }

                }
        );
    }

    @Override
    public Fragment getItem(int i) {
        L.d("pageAdapter getItem: " + (i+1));
        ThreadContentPageFragment fragment = ThreadContentPageFragment.newInstance(i+1);

        mPresenter.setupListeners(fragment, i+1);

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        L.d("pageAdapter instantiateItem: " + (position+1));
        ThreadContentPageFragment fragment =
                (ThreadContentPageFragment) super.instantiateItem(container, position);

        if(fragment.isResumed()){
            mPresenter.setupListeners(fragment, position+1);
            mPresenter.loadPosts(position+1);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void refresh(int page) {
        mPresenter.loadPosts(page);
    }


    public Observable<Integer> getTotalPageUpdate() {
        return mPresenter.getTotalPageUpdate();
    }

}
