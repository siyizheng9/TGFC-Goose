package com.sora.zero.tgfc.view.threadContent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sora.zero.tgfc.App;
import com.sora.zero.tgfc.R;
import com.sora.zero.tgfc.data.event.QuoteEvent;
import com.sora.zero.tgfc.utils.L;
import com.sora.zero.tgfc.view.base.BaseFragment;
import com.sora.zero.tgfc.view.MainActivity;
import com.sora.zero.tgfc.widget.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by zsy on 4/1/17.
 */

public class ThreadContentFragment extends BaseFragment{


    private static final String ARG_TID = "tid";
    private static final String ARG_FID = "fid";
    private static final String ARG_TITLE = "title";
    private static final String TOTAL_PAGE = "total_page";
    private static final String CURRENT_PAGE = "current_page";

    private ViewPager mViewPager;
    private ContentViewPagerAdapter mFragmentPagerAdapter;
    private CompositeDisposable mCompositeDisposable;
    private MenuItem gotoPage;
    private int mTotalPage;
    private int mCurrentPage;
    public int tid;
    public int fid;
    private EventBus mEventBus;

    @Override
    public void onAttach(Context context){
        this.LOG_TAG = "ThreadContentFragment";
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        L.d(LOG_TAG, "onCreateView");
        if(savedInstanceState == null) {
            Bundle bundle = getArguments();
            this.tid = bundle.getInt(ARG_TID);
            this.fid = bundle.getInt(ARG_FID);
            this.fragmentTitle = bundle.getString(ARG_TITLE);
            this.mTotalPage = 1;
            this.mCurrentPage = 1;
        } else {
            this.tid = savedInstanceState.getInt(ARG_TID);
            this.fid = savedInstanceState.getInt(ARG_FID);
            this.fragmentTitle = savedInstanceState.getString(ARG_TITLE);
            this.mTotalPage = savedInstanceState.getInt(TOTAL_PAGE);
            this.mCurrentPage = savedInstanceState.getInt(CURRENT_PAGE);
            showGotoPageDialog();
        }

        mEventBus = App.getAppComponent().getEventBus();

        View pagerView = inflater.inflate(R.layout.fragment_view_pager, parent, false);
        this.mViewPager = (ViewPager) pagerView.findViewById(R.id.view_pager);
        this.mFragmentPagerAdapter = new ContentViewPagerAdapter(getChildFragmentManager(), tid);
        this.mFragmentPagerAdapter.setTotalPage(mTotalPage);
        this.mViewPager.setAdapter(mFragmentPagerAdapter);

        Disposable disposable = this.mFragmentPagerAdapter.getTotalPageUpdate().subscribe(
                //onNext
                totalPage -> {
                    if(this.mTotalPage != totalPage && totalPage != 0){
                        this.mTotalPage = totalPage;
                        updateTitle();
                    }
                    if(mTotalPage > 1)
                        gotoPage.setEnabled(true);
                    else
                        gotoPage.setEnabled(false);
                }

        );

        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(disposable);

        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                                    @Override
                                                    public void onPageScrolled(int position, float positionOffset,
                                                            int positionOffsetPixels) { }

                                                    @Override
                                                    public void onPageSelected(int position) {
                                                        mCurrentPage = position+1;
                                                        updateTitle();
                                                    }

                                                    @Override
                                                    public void onPageScrollStateChanged(int state) { }
                                                });

        this.mViewPager.setCurrentItem(mCurrentPage - 1);
        return pagerView;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mTotalPage == 0)
            getActivity().setTitle(fragmentTitle);
        else
            updateTitle();
        Disposable quoteDisposable = mEventBus.get()
                .ofType(QuoteEvent.class)
                .subscribe(quoteEvent -> {
                    //onNext
                    L.d("quote floor " + quoteEvent.getQuotePost().getFloorNum());
                });
       mCompositeDisposable.add(quoteDisposable);

    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeDisposable.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_TID, this.tid);
        outState.putSerializable(ARG_FID, this.fid);
        outState.putSerializable(ARG_TITLE, this.fragmentTitle);
        outState.putSerializable(CURRENT_PAGE, this.mCurrentPage);
        outState.putSerializable(TOTAL_PAGE, this.mTotalPage);

        super.onSaveInstanceState(outState);
    }

    public static ThreadContentFragment newInstance(int tid, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TID, tid);
        bundle.putString(ARG_TITLE, title);
        ThreadContentFragment fragment = new ThreadContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.thread_content, menu);

        gotoPage = menu.findItem(R.id.action_goto_page);

        ((MainActivity) getActivity()).setDrawerToggleIndicator(false);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.d(LOG_TAG + " onOptionsItemSelected itemId: " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_content_refresh:
                this.mFragmentPagerAdapter.refresh(mCurrentPage);
                return true;
            case R.id.action_goto_page:
                showGotoPageDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private ThreadContentPageFragment getCurrentFragment() {
        Fragment page = getChildFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.view_pager + ":" + mViewPager.getCurrentItem());

        return (ThreadContentPageFragment) page;

    }

    private void showGotoPageDialog() {
        FragmentManager fm = getChildFragmentManager();
        GotoPageDialogFragment dialogFragment = (GotoPageDialogFragment) fm
                .findFragmentByTag(GotoPageDialogFragment.class.toString());

        if(dialogFragment == null){
            dialogFragment = GotoPageDialogFragment.newInstance(mTotalPage, mCurrentPage);
            dialogFragment.show(fm, GotoPageDialogFragment.class.toString());
        }

        dialogFragment.getPageSelect().subscribe(
                //onNext
                page -> {
                    if (page != mCurrentPage && page > 0) {
                        mViewPager.setCurrentItem(page - 1);
                    }
                });

    }

    private void updateTitle() {
        String newTitle = "(" + mCurrentPage + "/" + mTotalPage + ")" + fragmentTitle;
        getActivity().setTitle(newTitle);
    }

}
